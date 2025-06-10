package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @Column(name = "codigo_curso")
    private String codigoCurso;

    @Column(name = "nivel")
    private String nivel;  // PRIMARIA o SECUNDARIA

    // Relación inversa si querés listar profesores por curso
    @OneToMany(mappedBy = "curso")
    private List<Profesor> profesores;
}