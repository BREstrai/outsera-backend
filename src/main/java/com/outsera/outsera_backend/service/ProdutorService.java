package com.outsera.outsera_backend.service;

import com.outsera.outsera_backend.dto.IntervaloDTO;
import com.outsera.outsera_backend.dto.IntervaloRetornoDTO;
import com.outsera.outsera_backend.dto.ProdutorVitoriaDTO;
import com.outsera.outsera_backend.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProdutorService {

    private final FilmeRepository filmeRepository;

    public IntervaloRetornoDTO obterIntervalosProdutor() {

        List<ProdutorVitoriaDTO> vitorias = filmeRepository.findVitoriasPorProdutor().stream()
                .map(row -> new ProdutorVitoriaDTO((String) row[0], (Integer) row[1]))
                .toList();

        if (vitorias.isEmpty()) {

            return IntervaloRetornoDTO.builder()
                    .min(Collections.emptyList())
                    .max(Collections.emptyList())
                    .build();
        }

        List<IntervaloDTO> intervalos = calcularIntervalos(vitorias);

        if (intervalos.isEmpty()) {

            return IntervaloRetornoDTO.builder()
                    .min(Collections.emptyList())
                    .max(Collections.emptyList())
                    .build();
        }

        return construirResultado(intervalos);
    }

    private List<IntervaloDTO> calcularIntervalos(List<ProdutorVitoriaDTO> vitorias) {

        List<IntervaloDTO> intervalos = new ArrayList<>();
        Map<String, Integer> ultimoAnoPorProdutor = new HashMap<>();

        for (ProdutorVitoriaDTO vitoria : vitorias) {

            String produtor = vitoria.nomeProdutor();
            Integer anoAtual = vitoria.ano();

            if (ultimoAnoPorProdutor.containsKey(produtor)) {

                Integer anoAnterior = ultimoAnoPorProdutor.get(produtor);

                intervalos.add(IntervaloDTO.builder()
                        .producer(produtor)
                        .interval(anoAtual - anoAnterior)
                        .previousWin(anoAnterior)
                        .followingWin(anoAtual)
                        .build());
            }

            ultimoAnoPorProdutor.put(produtor, anoAtual);
        }

        return intervalos;
    }

    private IntervaloRetornoDTO construirResultado(List<IntervaloDTO> intervalos) {

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (IntervaloDTO intervalo : intervalos) {

            min = Math.min(min, intervalo.getInterval());
            max = Math.max(max, intervalo.getInterval());
        }

        List<IntervaloDTO> intervalosMinimos = new ArrayList<>();
        List<IntervaloDTO> intervalosMaximos = new ArrayList<>();

        final int intervaloMinimo = min;
        final int intervaloMaximo = max;

        for (IntervaloDTO intervalo : intervalos) {

            if (intervalo.getInterval().equals(intervaloMinimo)) {

                intervalosMinimos.add(intervalo);
            }

            if (intervalo.getInterval().equals(intervaloMaximo)) {

                intervalosMaximos.add(intervalo);
            }
        }

        return IntervaloRetornoDTO.builder()
                .min(intervalosMinimos)
                .max(intervalosMaximos)
                .build();
    }
}
