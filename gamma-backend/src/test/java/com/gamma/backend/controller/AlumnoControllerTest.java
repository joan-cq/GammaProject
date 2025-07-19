package com.gamma.backend.controller;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Grado;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.GradoRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AlumnoService;
import com.gamma.backend.service.modelservice.AnioEscolarService;
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

class AlumnoControllerTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GradoRepository gradoRepository;

    @Mock
    private AnioEscolarService anioEscolarService;

    @Mock
    private AlumnoService alumnoService;

    @InjectMocks
    private AlumnoController alumnoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void listarAlumnos_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Grado grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        Alumno alumno1 = new Alumno();
        alumno1.setDni("123");
        alumno1.setNombre("Alumno");
        alumno1.setApellido("Uno");
        User user1 = new User();
        user1.setDni("123");
        user1.setRol("ALUMNO");
        alumno1.setUser(user1);
        alumno1.setGrado(grado);
        alumno1.setAnioEscolar(anioActivo);

        when(alumnoRepository.findAll()).thenReturn(List.of(alumno1));

        // Act
        ResponseEntity<List<Alumno>> response = alumnoController.listarAlumnos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ALUMNO", response.getBody().get(0).getRol());
    }

    @SuppressWarnings("null")
    @Test
    void actualizarAlumno_ReturnsOk() {
        // Arrange
        Grado grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        Alumno alumno = new Alumno();
        alumno.setDni("123");
        alumno.setNombre("Alumno");
        alumno.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setRol("ALUMNO");
        alumno.setUser(user);

        Map<String, Object> payload = Map.of("dni", "123", "nombre", "Alumno Actualizado", "apellido", "Uno", "celularApoderado", "123456789", "codigoGrado", "G001", "estado", "ACTIVO", "rol", "ALUMNO");

        when(alumnoRepository.findById("123")).thenReturn(Optional.of(alumno));
        when(gradoRepository.findById("G001")).thenReturn(Optional.of(grado));
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumno);

        // Act
        ResponseEntity<Alumno> response = alumnoController.actualizarAlumno(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alumno Actualizado", response.getBody().getNombre());
    }

    @Test
    void agregarAlumno_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Grado grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        Map<String, String> payload = Map.of("dni", "123", "nombre", "Alumno", "apellido", "Uno", "celularApoderado", "123456789", "genero", "M", "codigoGrado", "G001", "clave", "secreto");

        when(userRepository.existsById("123")).thenReturn(false);
        when(gradoRepository.findById("G001")).thenReturn(Optional.of(grado));
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));

        // Act
        ResponseEntity<?> response = alumnoController.agregarAlumno(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Alumno agregado con éxito"), response.getBody());
    }

    @Test
    void eliminarAlumno_ReturnsOk() {
        // Arrange
        Alumno alumno = new Alumno();
        alumno.setDni("123");
        alumno.setNombre("Alumno");
        alumno.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setRol("ALUMNO");
        alumno.setUser(user);

        when(alumnoRepository.findById("123")).thenReturn(Optional.of(alumno));

        // Act
        ResponseEntity<?> response = alumnoController.eliminarAlumno("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Alumno eliminado con éxito"), response.getBody());
    }

    @Test
    void actualizarPassword_ReturnsOk() {
        // Arrange
        Alumno alumno = new Alumno();
        alumno.setDni("123");
        alumno.setNombre("Alumno");
        alumno.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setClave("secreto");
        alumno.setUser(user);

        Map<String, String> payload = Map.of("nuevaClave", "nuevaSecreto");

        when(alumnoRepository.findById("123")).thenReturn(Optional.of(alumno));

        // Act
        ResponseEntity<?> response = alumnoController.actualizarPassword("123", payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Contraseña actualizada con éxito"), response.getBody());
    }

    @Test
    void obtenerCodigoCurso_ReturnsOk() {
        // Arrange
        when(alumnoService.obtenerCodigoGradoPorDni("123")).thenReturn("G001");

        // Act
        ResponseEntity<?> response = alumnoController.obtenerCodigoCurso("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("codigo_curso", "G001"), response.getBody());
    }
}