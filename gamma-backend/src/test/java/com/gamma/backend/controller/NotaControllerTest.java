package com.gamma.backend.controller;

import com.gamma.backend.model.*;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.repository.CursoRepository;
import com.gamma.backend.repository.NotaRepository;
import com.gamma.backend.service.modelservice.NotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotaControllerTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private BimestreRepository bimestreRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private NotaService notaService;

    @InjectMocks
    private NotaController notaController;

    private Alumno alumno;
    private Curso curso;
    private Bimestre bimestre;
    private Nota nota;
    private Grado grado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        alumno = new Alumno();
        alumno.setDni("12345678");
        alumno.setNombre("Juan");
        alumno.setApellido("Perez");
        alumno.setGrado(grado);

        curso = new Curso();
        curso.setCodigoCurso("C001");
        curso.setNombre("Matemáticas");

        bimestre = new Bimestre();
        bimestre.setId(1);
        bimestre.setNombre("Bimestre 1");

        nota = new Nota();
        nota.setId(1L);
        nota.setAlumno(alumno);
        nota.setCurso(curso);
        nota.setBimestre(bimestre);
        nota.setNota(15.5);
    }

    @Test
    void obtenerAlumnosPorGradoBimestreCurso_ReturnsOk() {
        // Arrange
        when(alumnoRepository.findByGrado_CodigoGrado("G001")).thenReturn(List.of(alumno));
        when(notaRepository.findByAlumno_DniAndCurso_CodigoCursoAndBimestre_Id("12345678", "C001", 1)).thenReturn(nota);

        // Act
        ResponseEntity<?> response = notaController.obtenerAlumnosPorGradoBimestreCurso("G001", 1, "C001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Map<String, Object>> body = (List<Map<String, Object>>) response.getBody();
        assertEquals(1, body.size());
        assertEquals("Juan", body.get(0).get("nombre"));
        assertEquals(15.5, body.get(0).get("nota"));
    }

    @Test
    void agregarNota_ReturnsOk() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("dniAlumno", "12345678");
        payload.put("codigoCurso", "C001");
        payload.put("idBimestre", "1");
        payload.put("nota", "15.5");

        when(alumnoRepository.findByDni("12345678")).thenReturn(alumno);
        when(cursoRepository.findById("C001")).thenReturn(Optional.of(curso));
        when(bimestreRepository.findById(1L)).thenReturn(Optional.of(bimestre));

        // Act
        ResponseEntity<?> response = notaController.agregarNota(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Nota agregada correctamente"), response.getBody());
        verify(notaService, times(1)).guardarNota(any(Nota.class));
    }

    @Test
    void actualizarNota_ReturnsOk() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("idNota", 1);
        payload.put("nota", "18.0");

        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        // Act
        ResponseEntity<?> response = notaController.actualizarNota(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Nota actualizada correctamente"), response.getBody());
        verify(notaService, times(1)).actualizarNota(any(Nota.class));
    }

    @Test
    void obtenerNotasPorAlumno_ReturnsOk() {
        // Arrange
        when(notaRepository.findAllByAlumno_Dni("12345678")).thenReturn(List.of(nota));

        // Act
        ResponseEntity<List<Map<String, Object>>> response = notaController.obtenerNotasPorAlumno("12345678");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Map<String, Object>> body = response.getBody();
        assertEquals(1, body.size());
        assertEquals(15.5, body.get(0).get("nota"));
        assertEquals("Matemáticas", body.get(0).get("curso"));
    }
}