package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.User;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.CursoRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import com.gamma.backend.service.modelservice.ProfesorService;
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

class ProfesorControllerTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnioEscolarService anioEscolarService;

    @Mock
    private ProfesorService profesorService;

    @InjectMocks
    private ProfesorController profesorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void listarProfesores_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Curso curso = new Curso();
        curso.setCodigoCurso("C001");
        curso.setNombre("Matemáticas");

        Profesor profesor1 = new Profesor();
        profesor1.setDni("123");
        profesor1.setNombre("Profesor");
        profesor1.setApellido("Uno");
        User user1 = new User();
        user1.setDni("123");
        user1.setRol("PROFESOR");
        profesor1.setUser(user1);
        profesor1.setCurso(curso);
        profesor1.setAnioEscolar(anioActivo);

        when(profesorRepository.findAll()).thenReturn(List.of(profesor1));

        // Act
        ResponseEntity<List<Profesor>> response = profesorController.listarProfesores();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("PROFESOR", response.getBody().get(0).getRol());
        assertEquals("Matemáticas", response.getBody().get(0).getCodigoCurso());
    }

    @SuppressWarnings("null")
    @Test
    void actualizarProfesor_ReturnsOk() {
        // Arrange
        Profesor profesor = new Profesor();
        profesor.setDni("123");
        profesor.setNombre("Profesor");
        profesor.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setRol("PROFESOR");
        profesor.setUser(user);
        Curso curso = new Curso();
        curso.setCodigoCurso("MAT101");
        profesor.setCurso(curso);

        Map<String, Object> payload = Map.of("dni", "123", "nombre", "Profesor", "apellido", "Uno", "celular", "123456789", "curso", "MAT101", "estado", "activo", "rol", "PROFESOR");

        when(profesorRepository.findById("123")).thenReturn(Optional.of(profesor));
        when(cursoRepository.findById("MAT101")).thenReturn(Optional.of(curso));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesor);

        // Act
        ResponseEntity<Profesor> response = profesorController.actualizarProfesor(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profesor", response.getBody().getNombre());
        assertEquals("MAT101", response.getBody().getCurso().getCodigoCurso());
    }

    @Test
    void agregarProfesor_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Curso curso = new Curso();
        curso.setCodigoCurso("C001");
        curso.setNombre("Matemáticas");

        Map<String, String> payload = Map.of("dni", "123", "nombre", "Profesor", "apellido", "Uno", "celular", "123456789", "curso", "C001", "clave", "secreto");

        when(userRepository.existsById("123")).thenReturn(false);
        when(cursoRepository.findById("C001")).thenReturn(Optional.of(curso));
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));

        // Act
        ResponseEntity<?> response = profesorController.agregarProfesor(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Profesor agregado con éxito"), response.getBody());
    }

    @Test
    void eliminarProfesor_ReturnsOk() {
        // Arrange
        Profesor profesor = new Profesor();
        profesor.setDni("12345678");
        profesor.setNombre("Profesor");
        profesor.setApellido("Uno");
        User user = new User();
        user.setDni("12345678");
        user.setRol("PROFESOR");
        profesor.setUser(user);

        when(profesorRepository.findById("123")).thenReturn(Optional.of(profesor));

        // Act
        ResponseEntity<?> response = profesorController.eliminarProfesor("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Profesor eliminado con éxito"), response.getBody());
    }

    @Test
    void actualizarPassword_ReturnsOk() {
        // Arrange
        Profesor profesor = new Profesor();
        profesor.setDni("123");
        profesor.setNombre("Profesor");
        profesor.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setClave("secreto");
        profesor.setUser(user);

        Map<String, String> payload = Map.of("nuevaClave", "nuevaSecreto");

        when(profesorRepository.findById("123")).thenReturn(Optional.of(profesor));

        // Act
        ResponseEntity<?> response = profesorController.actualizarPassword("123", payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Contraseña actualizada con éxito"), response.getBody());
    }

    @Test
    void obtenerCodigoCurso_ReturnsOk() {
        // Arrange
        when(profesorService.obtenerCodigoCursoPorDni("123")).thenReturn("C001");

        // Act
        ResponseEntity<?> response = profesorController.obtenerCodigoCurso("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("codigo_curso", "C001"), response.getBody());
    }
}