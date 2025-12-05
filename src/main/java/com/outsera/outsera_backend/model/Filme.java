package com.outsera.outsera_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "filme_produtor",
            joinColumns = @JoinColumn(name = "filme_id"),
            inverseJoinColumns = @JoinColumn(name = "produtor_id")
    )
    @Builder.Default
    private Set<Produtor> produtores = new HashSet<>();
}
