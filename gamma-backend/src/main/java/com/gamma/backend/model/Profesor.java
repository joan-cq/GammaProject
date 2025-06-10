package com.gamma.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profesor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    @Id
    @Column(name = "dni")
    private String dni;

    @OneToOne
    @JoinColumn(name = "dni", referencedColumnName = "dni")
    private User user;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "celular")
    private String celular;

    @ManyToOne
    @JoinColumn(name = "codigo_curso")
    @JsonBackReference
    private Curso curso;

    @Column(name = "estado")
    private String estado;

    public String getRol() {
        return this.user.getRol();
    }

    public String getClave() {
        return this.user.getClave();
    }

    public String getCodigoCurso() {
        return this.curso.getCodigoCurso();
    }
    public void setRol(String rol) {
        this.user.setRol(rol);
    }

    public void setClave(String clave) {
        this.user.setClave(clave);
    }

    public void setCodigoCurso(String codigoCurso) {
        this.curso.setCodigoCurso(codigoCurso);
    }
}