package com.gamma.backend.repository;

import com.gamma.backend.model.AnioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnioRepository extends JpaRepository<AnioEscolar, Integer> {
}