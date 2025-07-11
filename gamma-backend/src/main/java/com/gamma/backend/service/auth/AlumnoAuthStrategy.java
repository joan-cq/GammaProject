package com.gamma.backend.service.auth;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlumnoAuthStrategy implements RoleAuthStrategy {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public String getRol() {
        return "ALUMNO";
    }

    @Override
    public boolean estaActivo(String dni, Integer anioActivo) {
        Alumno alumno = alumnoRepository.findByDni(dni);
        return alumno != null &&
            "ACTIVO".equalsIgnoreCase(alumno.getEstado()) &&
            alumno.getAnioEscolar() != null &&
            alumno.getAnioEscolar().getId().equals(anioActivo);
    }
}
