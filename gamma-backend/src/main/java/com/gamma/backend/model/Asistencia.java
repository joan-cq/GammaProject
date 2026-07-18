package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "asistencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dni_alumno")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "codigo_curso")
    private Curso curso;

    @Column(name = "fecha")
    private LocalDate fecha;

    // PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO
    @Column(name = "estado")
    private String estado;
}
