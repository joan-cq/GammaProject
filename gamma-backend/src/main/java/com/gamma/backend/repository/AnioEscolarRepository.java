package com.gamma.backend.repository;

import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AnioEscolarRepository extends JpaRepository<AnioEscolar, Integer> {
    
    // Buscar por año (ej. 2025)
    Optional<AnioEscolar> findByAnio(int anio);

    // Buscar por estado: "ACTIVO" o "INACTIVO"
    List<AnioEscolar> findByEstado(String estado);
}
