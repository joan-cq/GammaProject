package com.gamma.backend.repository;

import com.gamma.backend.model.GradoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoCursoRepository extends JpaRepository<GradoCurso, Long> {
    List<GradoCurso> findByCurso_CodigoCursoAndAnioEscolar_IdAndEstado(String codigoCurso, Integer anioEscolarId, String estado);
    List<GradoCurso> findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado(String grado, Integer anioEscolarId, String estado);
    List<GradoCurso> findByGrado_CodigoGradoAndCurso_CodigoCursoAndAnioEscolar_IdAndEstado(String grado, String codigoCurso, Integer anioEscolarId, String estado);
}

