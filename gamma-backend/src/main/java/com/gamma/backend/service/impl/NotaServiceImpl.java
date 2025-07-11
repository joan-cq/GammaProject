package com.gamma.backend.service.impl;

import com.gamma.backend.model.Nota;
import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.Curso;
import com.gamma.backend.model.GradoCurso;
import com.gamma.backend.model.Profesor;
import com.gamma.backend.repository.GradoCursoRepository;

import java.util.ArrayList;
import com.gamma.backend.repository.*;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import com.gamma.backend.service.modelservice.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final BimestreRepository bimestreRepository;
    private final AnioEscolarService anioEscolarService;
    private final ProfesorRepository profesorRepository;

    @Autowired
    public NotaServiceImpl(NotaRepository notaRepository, AlumnoRepository alumnoRepository,
                           CursoRepository cursoRepository, BimestreRepository bimestreRepository,
                           AnioEscolarService anioEscolarService, ProfesorRepository profesorRepository) {
        this.notaRepository = notaRepository;
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
        this.bimestreRepository = bimestreRepository;
        this.anioEscolarService = anioEscolarService;
        this.profesorRepository = profesorRepository;
    }

    @Override
    public void guardarNota(Nota nota) {
        notaRepository.save(nota);
    }

    @Override
    public void actualizarNota(Nota nota) {
        Nota existente = notaRepository.findById(nota.getIdNota()).orElseThrow();
        existente.setNota(nota.getNota());
        notaRepository.save(existente);
    }
    @Autowired
    private GradoCursoRepository gradoCursoRepository;

    @Override
    public List<Nota> obtenerNotasPorGradoYBimestre(String codigoGrado, Long idBimestre, String dniProfesor) {
        List<Nota> notas = new ArrayList<>();
        Bimestre bimestre = bimestreRepository.findById(idBimestre).orElse(null);
        if (bimestre == null) {
            return new ArrayList<>();
        }

        AnioEscolar anioActivo = anioEscolarService.obtenerAnioActivo().orElse(null);
        if (anioActivo == null) {
            return new ArrayList<>();
        }

        List<Alumno> alumnos = alumnoRepository.findByGrado(codigoGrado);
        for (Alumno alumno : alumnos) {
            Optional<Nota> notaOptional = notaRepository.findByAlumnoAndBimestre(alumno, bimestre);
            notaOptional.ifPresent(notas::add);
        }

        return notas;
    }

    @Override
    public List<Nota> obtenerNotasPorCursoYBimestre(Curso curso, Bimestre bimestre) {
        return notaRepository.findByCursoAndBimestre(curso, bimestre);
    }

    @Override
    public Optional<Nota> obtenerNotaPorAlumnoCursoYBimestre(Alumno alumno, Curso curso, Bimestre bimestre) {
        return notaRepository.findByAlumnoAndCursoAndBimestre(alumno, curso, bimestre);
    }

    @Override
    public Nota agregarNota(Nota nota) {
        return notaRepository.save(nota);
    }

}
