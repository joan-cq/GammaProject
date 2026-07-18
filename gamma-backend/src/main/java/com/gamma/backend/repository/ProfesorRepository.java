package com.gamma.backend.repository;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String> {
    Profesor findByDni(String dni);
    List<Profesor> findByAnioEscolar(AnioEscolar anioEscolar);
    List<Profesor> findByAnioEscolarIn(List<AnioEscolar> anios);
    List<Profesor> findByAnioEscolar_Anio(int anio);
}
