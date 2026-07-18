package com.gamma.backend.repository;

import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BimestreRepository extends JpaRepository<Bimestre, Long> {

    // Obtener todos los bimestres por año escolar
    List<Bimestre> findByAnioEscolar(AnioEscolar anioEscolar);

    // Buscar por nombre del bimestre dentro de un año escolar
    Bimestre findByNombreAndAnioEscolar(String nombre, AnioEscolar anioEscolar);
}
