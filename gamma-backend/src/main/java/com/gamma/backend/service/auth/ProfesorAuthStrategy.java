package com.gamma.backend.service.auth;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfesorAuthStrategy implements RoleAuthStrategy {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    public String getRol() {
        return "PROFESOR";
    }

    @Override
    public boolean estaActivo(String dni, Integer anioActivo) {
        Profesor profesor = profesorRepository.findByDni(dni);
        return profesor != null &&
            "ACTIVO".equalsIgnoreCase(profesor.getEstado()) &&
            profesor.getAnioEscolar() != null &&
            profesor.getAnioEscolar().getId().equals(anioActivo);
    }
}