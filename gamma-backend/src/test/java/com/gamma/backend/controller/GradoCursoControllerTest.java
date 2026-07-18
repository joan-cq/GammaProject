package com.gamma.backend.controller;

import com.gamma.backend.model.*;
import com.gamma.backend.repository.GradoCursoRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradoCursoControllerTest {

    @Mock
    private GradoCursoRepository gradoCursoRepository;

    @Mock
    private AnioEscolarService anioEscolarService;

    @InjectMocks
    private GradoCursoController gradoCursoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para GET /grado_curso/filtrar/{codigoCurso}
    @Test
    void testListarGradosPorCurso() {
        // Arrange
        String codigoCurso = "MAT";
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(2025);

        Grado grado1 = new Grado();
        grado1.setCodigoGrado("G1");
        grado1.setNombreGrado("Primero");

        GradoCurso relacion = new GradoCurso();
        relacion.setCurso(new Curso());
        relacion.setGrado(grado1);
        relacion.setAnioEscolar(anioActivo);
        relacion.setEstado("ACTIVO");

        List<GradoCurso> relaciones = List.of(relacion);

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(gradoCursoRepository.findByCurso_CodigoCursoAndAnioEscolar_IdAndEstado(codigoCurso, 2025, "ACTIVO"))
                .thenReturn(relaciones);

        // Act
        List<Grado> resultado = gradoCursoController.listarGradosPorCurso(codigoCurso);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("G1", resultado.get(0).getCodigoGrado());
        verify(gradoCursoRepository).findByCurso_CodigoCursoAndAnioEscolar_IdAndEstado(codigoCurso, 2025, "ACTIVO");
    }

    // Test para GET /grado_curso/cursos/{codigoGrado}
    @Test
    void testListarCursosPorGrado() {
        // Arrange
        String codigoGrado = "G2";
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(2025);

        Curso curso = new Curso();
        curso.setCodigoCurso("LEN");
        curso.setNombre("Lenguaje");

        GradoCurso relacion = new GradoCurso();
        relacion.setCurso(curso);
        relacion.setGrado(new Grado());
        relacion.setAnioEscolar(anioActivo);
        relacion.setEstado("ACTIVO");

        List<GradoCurso> relaciones = List.of(relacion);

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(gradoCursoRepository.findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado(codigoGrado, 2025, "ACTIVO"))
                .thenReturn(relaciones);

        // Act
        List<Curso> resultado = gradoCursoController.listarCursosPorGrado(codigoGrado);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("LEN", resultado.get(0).getCodigoCurso());
        verify(gradoCursoRepository).findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado(codigoGrado, 2025, "ACTIVO");
    }

    @Test
    void testListarCursosPorGrado_SinAnioActivo() {
        // Arrange
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.empty());

        // Act
        List<Curso> resultado = gradoCursoController.listarCursosPorGrado("G3");

        // Assert
        assertTrue(resultado.isEmpty());
        verify(gradoCursoRepository, never()).findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado(any(), anyInt(), any());
    }
}
