package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grado_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GradoCursoId.class)
public class GradoCurso {

    @Id
    @ManyToOne
    @JoinColumn(name = "codigo_grado")
    private Grado grado;

    @Id
    @ManyToOne
    @JoinColumn(name = "codigo_curso")
    private Curso curso;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_anio")
    private AnioEscolar anioEscolar;

    @Column(nullable = false)
    private String estado;
}