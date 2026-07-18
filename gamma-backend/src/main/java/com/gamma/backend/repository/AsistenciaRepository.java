package com.gamma.backend.repository;

import com.gamma.backend.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByAlumno_DniAndCurso_CodigoCursoAndFecha(
            String dniAlumno, String codigoCurso, LocalDate fecha);

    List<Asistencia> findAllByAlumno_DniOrderByFechaDesc(String dniAlumno);

    List<Asistencia> findAllByAlumno_DniAndCurso_CodigoCursoOrderByFechaDesc(
            String dniAlumno, String codigoCurso);
}
