package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Estado;
import com.gamma.backend.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CursoControllerTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarCursos_ReturnsOk() {
        // Arrange
        Curso curso1 = new Curso();
        curso1.setCodigoCurso("C001");
        curso1.setNombre("Matemáticas");
        curso1.setEstado(Estado.ACTIVO);

        Curso curso2 = new Curso();
        curso2.setCodigoCurso("C002");
        curso2.setNombre("Comunicación");
        curso2.setEstado(Estado.ACTIVO);

        when(cursoRepository.findAll()).thenReturn(List.of(curso1, curso2));

        // Act
        ResponseEntity<List<Curso>> response = cursoController.listarCursos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void agregarCurso_ReturnsOk() {
        // Arrange
        Curso curso = new Curso();
        curso.setCodigoCurso("C001");
        curso.setNombre("Matemáticas");

        // Act
        ResponseEntity<?> response = cursoController.agregarCurso(curso);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Curso agregado con éxito"), response.getBody());
        verify(cursoRepository, times(1)).save(curso);
        assertEquals(Estado.ACTIVO, curso.getEstado());
    }

    @Test
    void actualizarCurso_ReturnsOk() {
        // Arrange
        Curso cursoExistente = new Curso();
        cursoExistente.setCodigoCurso("C001");
        cursoExistente.setNombre("Matemáticas");
        cursoExistente.setEstado(Estado.ACTIVO);

        Curso cursoDetails = new Curso();
        cursoDetails.setNombre("Matemáticas Avanzadas");
        cursoDetails.setEstado(Estado.INACTIVO);

        when(cursoRepository.findById("C001")).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoExistente);

        // Act
        ResponseEntity<?> response = cursoController.actualizarCurso("C001", cursoDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Curso actualizado con éxito"), response.getBody());
        verify(cursoRepository, times(1)).save(cursoExistente);
        assertEquals("Matemáticas Avanzadas", cursoExistente.getNombre());
        assertEquals(Estado.INACTIVO, cursoExistente.getEstado());
    }

    @Test
    void toggleCurso_ReturnsOk() {
        // Arrange
        Curso cursoExistente = new Curso();
        cursoExistente.setCodigoCurso("C001");
        cursoExistente.setNombre("Matemáticas");
        cursoExistente.setEstado(Estado.ACTIVO);

        when(cursoRepository.findById("C001")).thenReturn(Optional.of(cursoExistente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoExistente);

        // Act
        ResponseEntity<?> response = cursoController.toggleCurso("C001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Estado del curso actualizado con éxito"), response.getBody());
        verify(cursoRepository, times(1)).save(cursoExistente);
        assertEquals(Estado.INACTIVO, cursoExistente.getEstado());
    }
}