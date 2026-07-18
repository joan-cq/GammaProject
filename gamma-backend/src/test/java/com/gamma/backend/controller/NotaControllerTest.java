package com.gamma.backend.controller;

import com.gamma.backend.model.*;
import com.gamma.backend.repository.*;
import com.gamma.backend.service.LogService;
import com.gamma.backend.service.modelservice.NotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotaControllerTest {

    @Mock private NotaRepository notaRepository;
    @Mock private AlumnoRepository alumnoRepository;
    @Mock private BimestreRepository bimestreRepository;
    @Mock private CursoRepository cursoRepository;
    @Mock private NotaService notaService;
    @Mock private LogService logService;

    @InjectMocks private NotaController notaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerAlumnosPorGradoBimestreCurso() {
        String codigoGrado = "G1";
        Integer idBimestre = 1;
        String codigoCurso = "MAT";

        Grado grado = new Grado();

        Alumno alumno = new Alumno();
        alumno.setDni("12345678");
        alumno.setNombre("Juan");
        alumno.setApellido("Pérez");
        alumno.setGrado(grado);

        Nota nota = new Nota();
        nota.setId(1L);
        nota.setNota(17.0);

        when(alumnoRepository.findByGrado_CodigoGrado(codigoGrado)).thenReturn(List.of(alumno));
        when(notaRepository.findByAlumno_DniAndCurso_CodigoCursoAndBimestre_Id("12345678", codigoCurso, idBimestre)).thenReturn(nota);

        ResponseEntity<?> response = notaController.obtenerAlumnosPorGradoBimestreCurso(codigoGrado, idBimestre, codigoCurso);
        List<Map<String, Object>> resultado = (List<Map<String, Object>>) response.getBody();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).get("nombre"));
        assertEquals(17.0, resultado.get(0).get("nota"));
    }

    @Test
    void testAgregarNota() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("dniAlumno", "12345678");
        payload.put("codigoCurso", "MAT");
        payload.put("idBimestre", "1");
        payload.put("nota", "18.5");

        Alumno alumno = new Alumno();
        alumno.setDni("12345678");

        Curso curso = new Curso();
        curso.setCodigoCurso("MAT");

        Bimestre bimestre = new Bimestre();
        bimestre.setId(1);

        when(alumnoRepository.findByDni("12345678")).thenReturn(alumno);
        when(cursoRepository.findById("MAT")).thenReturn(Optional.of(curso));
        when(bimestreRepository.findById(1L)).thenReturn(Optional.of(bimestre));

        ResponseEntity<?> response = notaController.agregarNota(payload);
        Map<String, Object> result = (Map<String, Object>) response.getBody();

        assertEquals("Nota agregada correctamente", result.get("mensaje"));
        verify(notaService).guardarNota(any(Nota.class));
    }

    @Test
    void testActualizarNota() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("idNota", 1);
        payload.put("nota", "19.0");

        Nota nota = new Nota();
        nota.setId(1L);
        nota.setNota(15.0);

        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota));

        ResponseEntity<?> response = notaController.actualizarNota(payload);
        Map<String, Object> result = (Map<String, Object>) response.getBody();

        assertEquals("Nota actualizada correctamente", result.get("mensaje"));
        verify(notaService).actualizarNota(nota);
    }

    @Test
    void testObtenerNotasPorAlumno() {
        String dni = "12345678";

        Curso curso = new Curso();
        curso.setNombre("Matemáticas");

        Bimestre bimestre = new Bimestre();
        bimestre.setId(1);

        Nota nota = new Nota();
        nota.setId(1L);
        nota.setNota(17.5);
        nota.setCurso(curso);
        nota.setBimestre(bimestre);

        when(notaRepository.findAllByAlumno_Dni(dni)).thenReturn(List.of(nota));

        ResponseEntity<List<Map<String, Object>>> response = notaController.obtenerNotasPorAlumno(dni);
        List<Map<String, Object>> resultado = response.getBody();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(17.5, resultado.get(0).get("nota"));
        assertEquals("Matemáticas", resultado.get(0).get("curso"));
        assertEquals(1, resultado.get(0).get("bimestre"));
    }
}
