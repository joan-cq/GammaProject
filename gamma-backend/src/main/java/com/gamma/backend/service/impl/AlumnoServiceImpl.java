package com.gamma.backend.service.impl;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.service.modelservice.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public String obtenerCodigoGradoPorDni(String dni) {
        Alumno alumno = alumnoRepository.findByDni(dni);
        if (alumno != null && alumno.getGrado() != null) {
            return alumno.getGrado().getCodigoGrado();
        }
        return null;
    }
}