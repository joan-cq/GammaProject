package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @Column(name = "codigo_curso", length = 10)
    private String codigoCurso;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String estado;
}