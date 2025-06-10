package com.gamma.backend.repository;

import com.gamma.backend.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, String> {
    Alumno findByDni(String dni);
}
