package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Grado;
import com.gamma.backend.model.GradoCurso;
import com.gamma.backend.repository.GradoCursoRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GradoCursoControllerTest {

    @Mock
    private GradoCursoRepository gradoCursoRepository;

    @Mock
    private AnioEscolarService anioEscolarService;

    @InjectMocks
    private GradoCursoController gradoCursoController;

    private AnioEscolar anioActivo;
    private Grado grado;
    private Curso curso;
    private GradoCurso gradoCurso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");
        grado.setNivel("Primaria");

        curso = new Curso();
        curso.setCodigoCurso("C001");
        curso.setNombre("Matemáticas");

        gradoCurso = new GradoCurso();
        gradoCurso.setGrado(grado);
        gradoCurso.setCurso(curso);
        gradoCurso.setAnioEscolar(anioActivo);
        gradoCurso.setEstado("ACTIVO");
    }

    @Test
    void listarGradosPorCurso_ReturnsListOfGrados() {
        // Arrange
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(gradoCursoRepository.findByCurso_CodigoCursoAndAnioEscolar_IdAndEstado("C001", 1, "ACTIVO"))
                .thenReturn(List.of(gradoCurso));

        // Act
        List<Grado> response = gradoCursoController.listarGradosPorCurso("C001");

        // Assert
        assertEquals(1, response.size());
        assertEquals("G001", response.get(0).getCodigoGrado());
    }

    @Test
    void listarCursosPorGrado_ReturnsListOfCursos() {
        // Arrange
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(gradoCursoRepository.findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado("G001", 1, "ACTIVO"))
                .thenReturn(List.of(gradoCurso));

        // Act
        List<Curso> response = gradoCursoController.listarCursosPorGrado("G001");

        // Assert
        assertEquals(1, response.size());
        assertEquals("C001", response.get(0).getCodigoCurso());
    }
}