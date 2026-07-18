package com.gamma.backend.service.modelservice;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.AnioEscolarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnioEscolarService {

    @Autowired
    private AnioEscolarRepository anioEscolarRepository;

    // Obtener el año escolar activo más reciente (si solo hubiera uno activo)
    public Optional<AnioEscolar> obtenerAnioActivo() {
        return anioEscolarRepository.findByEstado("ACTIVO").stream().findFirst();
    }

    // Obtener todos los años escolares activos
    public List<AnioEscolar> obtenerAniosActivos() {
        return anioEscolarRepository.findByEstado("ACTIVO");
    }

    // Obtener el ID de un año activo (si solo esperas uno)
    public Integer getIdAnioEscolarActivo() {
        return obtenerAnioActivo()
                .map(AnioEscolar::getId)
                .orElseThrow(() -> new RuntimeException("No hay año escolar activo"));
    }

    // Obtener entidad por año (ej. 2025)
    public Optional<AnioEscolar> obtenerPorAnio(int anio) {
        return anioEscolarRepository.findByAnio(anio);
    }
}
