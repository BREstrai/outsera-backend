package com.outsera.outsera_backend.repository;

import com.outsera.outsera_backend.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByVencedorTrue();
}
