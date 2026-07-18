package com.gamma.backend.repository;

import com.gamma.backend.model.Grado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradoRepository extends JpaRepository<Grado, String> {
}