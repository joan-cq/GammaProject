package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grado {

    @Id
    @Column(name = "codigo_grado", length = 10)
    private String codigoGrado;

    @Column(name = "nombre_grado")
    private String nombreGrado;

    @Column(nullable = false)
    private String nivel;
}