package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

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
    private Integer id;

    @Column(name = "anio", unique = true)
    private Integer anio;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "anioEscolar")
    private List<Nota> notas;
}