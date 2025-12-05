package com.outsera.outsera_backend.repository;

import com.outsera.outsera_backend.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

    List<Filme> findByVencedorTrue();

    @Query(value = """
            SELECT
                P.NOME,
                F.ANO
            FROM
                FILMES F
                INNER JOIN FILME_PRODUTOR FP ON
                    F.ID = FP.FILME_ID
                INNER JOIN PRODUTORES P ON
                    FP.PRODUTOR_ID = P.ID
            WHERE
                F.VENCEDOR = TRUE
            ORDER BY
                P.NOME,
                F.ANO
            """, nativeQuery = true)
    List<Object[]> findVitoriasPorProdutor();
}
