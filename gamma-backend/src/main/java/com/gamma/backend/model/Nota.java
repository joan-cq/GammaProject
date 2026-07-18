package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dni_alumno")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "codigo_curso")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_bimestre")
    private Bimestre bimestre;

    private Double nota;

    public Long getIdNota() {
        return id;
    }
}
