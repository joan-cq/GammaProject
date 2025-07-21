package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Estado;
import com.gamma.backend.repository.CursoRepository;
import com.gamma.backend.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.http.ResponseEntity;

class CursoControllerTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test GET /curso/list
    @Test
    void testListarCursos() {
        Curso curso1 = new Curso();
        curso1.setCodigoCurso("MAT");
        curso1.setNombre("Matemáticas");

        Curso curso2 = new Curso();
        curso2.setCodigoCurso("LEN");
        curso2.setNombre("Lenguaje");

        List<Curso> cursos = Arrays.asList(curso1, curso2);

        when(cursoRepository.findAll()).thenReturn(cursos);

        ResponseEntity<List<Curso>> response = cursoController.listarCursos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(logService).addLog(eq("INFO"), contains("listado los cursos"));
    }

    // Test POST /curso/add
    @Test
    void testAgregarCurso() {
        Curso nuevoCurso = new Curso();
        nuevoCurso.setCodigoCurso("HIS");
        nuevoCurso.setNombre("Historia");

        ResponseEntity<?> response = cursoController.agregarCurso(nuevoCurso);

        assertEquals(200, response.getStatusCodeValue());
        verify(cursoRepository).save(any(Curso.class));
        verify(logService).addLog(eq("INFO"), contains("nuevo curso"));
        assertEquals("Curso agregado con éxito", ((Map<?, ?>) response.getBody()).get("mensaje"));
    }

    // Test PUT /curso/update/{codigoCurso}
    @Test
    void testActualizarCursoExistente() {
        String codigoCurso = "MAT";
        Curso existente = new Curso();
        existente.setCodigoCurso(codigoCurso);
        existente.setNombre("Matemáticas");
        existente.setEstado(Estado.ACTIVO);

        Curso actualizado = new Curso();
        actualizado.setNombre("Matemáticas Avanzadas");
        actualizado.setEstado(Estado.INACTIVO);

        when(cursoRepository.findById(codigoCurso)).thenReturn(Optional.of(existente));

        ResponseEntity<?> response = cursoController.actualizarCurso(codigoCurso, actualizado);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Curso actualizado con éxito", ((Map<?, ?>) response.getBody()).get("mensaje"));
        verify(cursoRepository).save(existente);
        assertEquals("Matemáticas Avanzadas", existente.getNombre());
        assertEquals(Estado.INACTIVO, existente.getEstado());
    }

    @Test
    void testActualizarCursoNoExistente() {
        when(cursoRepository.findById("NOEXISTE")).thenReturn(Optional.empty());

        ResponseEntity<?> response = cursoController.actualizarCurso("NOEXISTE", new Curso());

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test PUT /curso/toggle/{codigoCurso}
    @Test
    void testToggleCurso() {
        Curso curso = new Curso();
        curso.setCodigoCurso("LEN");
        curso.setEstado(Estado.ACTIVO);

        when(cursoRepository.findById("LEN")).thenReturn(Optional.of(curso));

        ResponseEntity<?> response = cursoController.toggleCurso("LEN");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Estado del curso actualizado con éxito", ((Map<?, ?>) response.getBody()).get("mensaje"));
        assertEquals(Estado.INACTIVO, curso.getEstado()); // Verifica que cambió
        verify(cursoRepository).save(curso);
    }

    @Test
    void testToggleCursoNoEncontrado() {
        when(cursoRepository.findById("NOEXISTE")).thenReturn(Optional.empty());

        ResponseEntity<?> response = cursoController.toggleCurso("NOEXISTE");

        assertEquals(404, response.getStatusCodeValue());
    }
}
