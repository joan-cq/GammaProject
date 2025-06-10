package com.gamma.backend.repository;

import com.gamma.backend.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String> {
    Profesor findByDni(String dni);
}
