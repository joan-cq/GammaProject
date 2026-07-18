package com.gamma.backend.repository;

import com.gamma.backend.model.Nota;
import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByCursoAndBimestre(Curso curso, Bimestre bimestre);
    Optional<Nota> findByAlumnoAndCursoAndBimestre(Alumno alumno, Curso curso, Bimestre bimestre);
    Optional<Nota> findByAlumnoAndBimestre(Alumno alumno, Bimestre bimestre);

    Nota findByAlumno_DniAndCurso_CodigoCursoAndBimestre_Id(String alumnoDni, String codigoCurso, Integer bimestreId);
    List<Nota> findAllByAlumno_Dni(String alumnoDni);
}
