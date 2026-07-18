package com.gamma.backend.service.impl;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.service.modelservice.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    public String obtenerCodigoCursoPorDni(String dni) {
        Profesor profesor = profesorRepository.findByDni(dni);
        if (profesor != null) {
            return profesor.getCodigoCurso();
        }
        return null;
    }
}