package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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
    @JoinColumn(name = "dni_alumno", referencedColumnName = "dni")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "codigo_curso", referencedColumnName = "codigo_curso")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_anio", referencedColumnName = "id_anio")
    private AnioEscolar anioEscolar;

    @Column(name = "nota")
    private BigDecimal nota;

    @Column(name = "editable")
    private Boolean editable;
}