package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @Column(name = "dni")
    private String dni;

    @OneToOne
    @JoinColumn(name = "dni", referencedColumnName = "dni", insertable = false, updatable = false)
    private User user;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "celular_apoderado")
    private String celularApoderado;

    @Column(name = "genero")
    private String genero;

    @Column(name = "grado")
    private String grado;

    @Column(name = "nivel")
    private String nivel;

    @ManyToOne
    @JoinColumn(name = "id_anio")
    private AnioEscolar anioEscolar;

    @Column(name = "estado")
    private String estado;

    public String getRol() {
        return this.user.getRol();
    }

    public String getClave() {
        return this.user.getClave();
    }

    public void setRol(String rol) {
        this.user.setRol(rol);
    }

    public void setClave(String clave) {
        this.user.setClave(clave);
    }
}
