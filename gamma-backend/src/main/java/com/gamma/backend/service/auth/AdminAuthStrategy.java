package com.gamma.backend.service.auth;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthStrategy implements RoleAuthStrategy {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }

    @Override
    public boolean estaActivo(String dni, Integer anioActivo) {
        Administrador administrador = administradorRepository.findByDni(dni);
        return administrador != null &&
            "ACTIVO".equalsIgnoreCase(administrador.getEstado()) &&
            administrador.getAnioEscolar() != null &&
            administrador.getAnioEscolar().getId().equals(anioActivo);
    }

}
