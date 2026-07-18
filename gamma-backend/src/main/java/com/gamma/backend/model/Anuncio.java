package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "anuncio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anuncio")
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "dni_autor", nullable = false)
    private String dniAutor;

    @Column(name = "nombre_autor")
    private String nombreAutor;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @PrePersist
    public void prePersist() {
        if (fechaPublicacion == null) {
            fechaPublicacion = LocalDateTime.now();
        }
    }
}
