package com.outsera.outsera_backend.repository;

import com.outsera.outsera_backend.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {

    Optional<Produtor> findByNome(String nome);
}
