package com.gamma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // INFO, CREACION, MODIFICACION, ELIMINACION, LOGIN, ERROR
    private String categoria;

    // ALUMNO, PROFESOR, ADMINISTRADOR, CURSO, NOTA, ASISTENCIA, ANUNCIO, ANIO_ESCOLAR, AUTH...
    private String entidad;

    @Column(name = "usuario_dni")
    private String usuarioDni;

    private String mensaje;

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getUsuarioDni() {
        return usuarioDni;
    }

    public void setUsuarioDni(String usuarioDni) {
        this.usuarioDni = usuarioDni;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
