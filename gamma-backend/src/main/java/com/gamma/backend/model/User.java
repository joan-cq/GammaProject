package com.gamma.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "dni")
    private String dni;  // La PK de la que parten los tipos de usuarios

    @Column(name = "clave", nullable = false)
    private String clave;  // usando Bcrypt

    @Column(name = "rol", nullable = false)
    private String rol;    // ADMINISTRADOR, PROFESOR, ALUMNO
}
