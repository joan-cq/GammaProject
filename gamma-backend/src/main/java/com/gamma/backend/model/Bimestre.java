package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bimestre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bimestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bimestre")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_anio")
    private AnioEscolar anioEscolar;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}