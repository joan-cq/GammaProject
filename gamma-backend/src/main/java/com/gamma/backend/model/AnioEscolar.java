package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anio_escolar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnioEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anio")
    private Long id;

    @Column(unique = true, nullable = false)
    private int anio;

    @Column(nullable = false)
    private String estado;
}
