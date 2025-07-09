package com.gamma.backend.repository;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, String> {
    Alumno findByDni(String dni);
    List<Alumno> findByAnioEscolar(AnioEscolar anioEscolar);
    List<Alumno> findByAnioEscolarIn(List<AnioEscolar> anios);
    List<Alumno> findByAnioEscolar_Anio(int anio);
}
