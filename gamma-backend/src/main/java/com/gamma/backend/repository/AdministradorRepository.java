package com.gamma.backend.repository;

import com.gamma.backend.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    Administrador findByDni(String dni);
}