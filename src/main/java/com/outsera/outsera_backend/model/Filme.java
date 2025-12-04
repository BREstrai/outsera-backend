package com.outsera.outsera_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "estudios")
    private String estudio;

    @Column(name = "produtores")
    private String produtor;

    @Column(name = "vencedor")
    private Boolean vencedor;
}
