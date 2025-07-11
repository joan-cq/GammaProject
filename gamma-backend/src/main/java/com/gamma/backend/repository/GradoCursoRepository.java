package com.gamma.backend.repository;

import com.gamma.backend.model.GradoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoCursoRepository extends JpaRepository<GradoCurso, Long> {
    List<GradoCurso> findByCursoCodigoCursoAndAnioEscolarIdAndEstado(String codigoCurso, Long anioEscolar, String estado);
    List<GradoCurso> findByGradoCodigoGradoAndAnioEscolarIdAndEstado(String grado, Long anioEscolar, String estado);
    List<GradoCurso> findByGradoCodigoGradoAndCursoCodigoCursoAndAnioEscolarIdAndEstado(String grado, String codigoCurso, Long anioEscolar, String estado);
}

