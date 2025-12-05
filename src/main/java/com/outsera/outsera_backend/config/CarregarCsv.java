package com.outsera.outsera_backend.config;

import com.outsera.outsera_backend.model.Filme;
import com.outsera.outsera_backend.model.Produtor;
import com.outsera.outsera_backend.repository.ProdutorRepository;
import com.outsera.outsera_backend.service.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarregarCsv {

    private static final String REGEX_SEPARADOR_PRODUTOR = "[;,]|\\s+and\\s+";

    private final MovieService movieService;
    private final ProdutorRepository produtorRepository;

    @Value("${csv.file.path:csv/Movielist.csv}")
    private String csvFilePath;

    @PostConstruct
    public void carregarDados() {

        List<Filme> filmes = new ArrayList<>();
        ClassPathResource recurso = new ClassPathResource(csvFilePath);

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(recurso.getInputStream(),
                StandardCharsets.UTF_8))) {

            String linha;
            boolean isPrimeiraLinha = true;

            while ((linha = leitor.readLine()) != null) {

                if (isPrimeiraLinha) {

                    isPrimeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(";", -1);

                if (campos.length >= 4) {

                    try {

                        Integer ano = Integer.parseInt(campos[0].trim());
                        String titulo = campos[1].trim();
                        String estudios = campos[2].trim();
                        String produtores = campos[3].trim();
                        Boolean vencedor = campos.length > 4 && StringUtils.hasText(campos[4])
                                && "yes".equalsIgnoreCase(campos[4].trim());

                        filmes.add(
                                Filme.builder()
                                        .ano(ano)
                                        .titulo(titulo)
                                        .estudio(estudios)
                                        .produtor(produtores)
                                        .vencedor(vencedor)
                                        .produtores(new HashSet<>())
                                        .build()
                        );
                    } catch (NumberFormatException e) {

                        log.warn("Pulando linha inválida: {}", linha);
                    }
                }
            }

            movieService.salvarTodos(filmes);
            log.info("Carregado {} filmes do CSV", filmes.size());

            popularProdutores(filmes);

        } catch (Exception e) {

            log.error("Error ao carregar CSV", e);
        }
    }

    private void popularProdutores(List<Filme> filmes) {

        for (Filme filme : filmes) {

            if (isNull(filme.getProdutor()) || filme.getProdutor().isEmpty()) {

                throw new RuntimeException("Filme com produtor nulo ou vazio, ajustar o CSV: " + filme.getTitulo());
            }

            String[] nomesProdutores = filme.getProdutor().split(REGEX_SEPARADOR_PRODUTOR);
            Set<Produtor> produtoresDoFilme = new HashSet<>();

            for (String nomeProdutor : nomesProdutores) {

                String nomeLimpo = nomeProdutor.trim();

                if (nomeLimpo.isEmpty()) {

                    continue;
                }

                Produtor produtor = produtorRepository.findByNome(nomeLimpo)
                        .orElseGet(() -> {

                            Produtor novo = Produtor.builder()
                                    .nome(nomeLimpo)
                                    .build();

                            produtorRepository.save(novo);

                            return novo;
                        });

                produtoresDoFilme.add(produtor);
            }

            filme.setProdutores(produtoresDoFilme);
        }

        movieService.salvarTodos(filmes);
    }
}
