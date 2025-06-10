package com.gamma.backend.service.auth;

public interface RoleAuthStrategy {
    String getRol(); // Ej: "ADMINISTRADOR"
    boolean estaActivo(String dni);
}