package com.outsera.outsera_backend.service;

import com.outsera.outsera_backend.model.Filme;
import com.outsera.outsera_backend.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final FilmeRepository filmeRepository;

    public List<Filme> buscarTodosVencedores() {

        return filmeRepository.findByVencedorTrue();
    }

    public void salvarTodos(List<Filme> filmes) {

        filmeRepository.saveAll(filmes);
    }
}
