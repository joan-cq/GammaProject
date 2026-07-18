package com.gamma.backend.service.modelservice;

import com.gamma.backend.model.Nota;
import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.Curso;

import java.util.List;
import java.util.Optional;

public interface NotaService {
    List<Nota> obtenerNotasPorCursoYBimestre(Curso curso, Bimestre bimestre);
    Optional<Nota> obtenerNotaPorAlumnoCursoYBimestre(Alumno alumno, Curso curso, Bimestre bimestre);
    Nota agregarNota(Nota nota);
    void guardarNota(Nota nota);
    void actualizarNota(Nota nota);
    List<Nota> obtenerNotasPorGradoYBimestre(String codigoGrado, Long idBimestre, String dniProfesor);
}
