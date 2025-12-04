package com.outsera.outsera_backend.service;

import com.outsera.outsera_backend.dto.IntervaloDTO;
import com.outsera.outsera_backend.dto.IntervaloRetornoDTO;
import com.outsera.outsera_backend.model.Filme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutorService {

    private static final String REGEX_SEPARADOR_PRODUTOR = "[;,]|\\s+and\\s+";

    private final MovieService movieService;

    public IntervaloRetornoDTO obterIntervalosProdutor() {

        List<Filme> filmesVencedores = movieService.buscarTodosVencedores();

        Map<String, List<Integer>> anosVitoriaPorProdutor = agruparAnosVitoriaPorProdutor(filmesVencedores);

        List<IntervaloDTO> intervalos = calcularTodosIntervalos(anosVitoriaPorProdutor);

        if (intervalos.isEmpty()) {

            return IntervaloRetornoDTO.builder()
                    .min(Collections.emptyList())
                    .max(Collections.emptyList())
                    .build();
        }

        return construirResultadoIntervalo(intervalos);
    }

    private Map<String, List<Integer>> agruparAnosVitoriaPorProdutor(List<Filme> filmes) {

        Map<String, List<Integer>> vitoriasProdutor = new HashMap<>();

        for (Filme filme : filmes) {

            List<String> produtores = extrairProdutores(filme.getProdutor());

            for (String produtor : produtores) {

                vitoriasProdutor.computeIfAbsent(produtor, k -> new ArrayList<>())
                        .add(filme.getAno());
            }
        }

        return vitoriasProdutor;
    }

    private List<String> extrairProdutores(String produtorString) {

        return Arrays.stream(produtorString.split(REGEX_SEPARADOR_PRODUTOR))
                .map(String::trim)
                .filter(produtor -> !produtor.isEmpty())
                .collect(Collectors.toList());
    }

    private List<IntervaloDTO> calcularTodosIntervalos(Map<String, List<Integer>> anosVitoriaPorProdutor) {

        List<IntervaloDTO> todosIntervalos = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : anosVitoriaPorProdutor.entrySet()) {

            if (entry.getValue().size() >= 2) {

                String produtor = entry.getKey();
                List<Integer> anos = entry.getValue();

                List<IntervaloDTO> intervalosDoProdutor = calcularIntervalosProdutor(produtor, anos);
                todosIntervalos.addAll(intervalosDoProdutor);
            }
        }

        return todosIntervalos;
    }

    private List<IntervaloDTO> calcularIntervalosProdutor(String produtor, List<Integer> anos) {

        List<Integer> anosOrdenados = new ArrayList<>(anos);
        Collections.sort(anosOrdenados);

        List<IntervaloDTO> intervalos = new ArrayList<>();

        for (int i = 0; i < anosOrdenados.size() - 1; i++) {

            int anoAnterior = anosOrdenados.get(i);
            int anoSeguinte = anosOrdenados.get(i + 1);
            int intervalo = anoSeguinte - anoAnterior;

            intervalos.add(IntervaloDTO.builder()
                    .producer(produtor)
                    .interval(intervalo)
                    .previousWin(anoAnterior)
                    .followingWin(anoSeguinte)
                    .build());
        }

        return intervalos;
    }

    private IntervaloRetornoDTO construirResultadoIntervalo(List<IntervaloDTO> intervalos) {

        int intervaloMinimo = encontrarIntervaloMinimo(intervalos);
        int intervaloMaximo = encontrarIntervaloMaximo(intervalos);

        List<IntervaloDTO> intervalosMinimos = filtrarPorIntervalo(intervalos, intervaloMinimo);
        List<IntervaloDTO> intervalosMaximos = filtrarPorIntervalo(intervalos, intervaloMaximo);

        return IntervaloRetornoDTO.builder()
                .min(intervalosMinimos)
                .max(intervalosMaximos)
                .build();
    }

    private int encontrarIntervaloMinimo(List<IntervaloDTO> intervalos) {

        return intervalos.stream()
                .mapToInt(IntervaloDTO::getInterval)
                .min()
                .orElseThrow();
    }

    private int encontrarIntervaloMaximo(List<IntervaloDTO> intervalos) {

        return intervalos.stream()
                .mapToInt(IntervaloDTO::getInterval)
                .max()
                .orElseThrow();
    }

    private List<IntervaloDTO> filtrarPorIntervalo(List<IntervaloDTO> intervalos, int intervaloAlvo) {

        return intervalos.stream()
                .filter(intervalo -> intervalo.getInterval() == intervaloAlvo)
                .collect(Collectors.toList());
    }
}
