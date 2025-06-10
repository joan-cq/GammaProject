package com.gamma.backend.controller;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.UserRepository;
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
        Alumno alumno1 = new Alumno();
        alumno1.setDni("123");
        alumno1.setNombre("Alumno");
        alumno1.setApellido("Uno");
        User user1 = new User();
        user1.setDni("123");
        user1.setRol("ALUMNO");
        alumno1.setUser(user1);

        Alumno alumno2 = new Alumno();
        alumno2.setDni("456");
        alumno2.setNombre("Alumno");
        alumno2.setApellido("Dos");
        User user2 = new User();
        user2.setDni("456");
        user2.setRol("ALUMNO");
        alumno2.setUser(user2);

        when(alumnoRepository.findAll()).thenReturn(List.of(alumno1, alumno2));

        // Act
        ResponseEntity<List<Alumno>> response = alumnoController.listarAlumnos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("ALUMNO", response.getBody().get(0).getRol());
    }

    @SuppressWarnings("null")
    @Test
    void actualizarAlumno_ReturnsOk() {
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
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumno);

        // Act
        ResponseEntity<Alumno> response = alumnoController.actualizarAlumno(alumno);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alumno", response.getBody().getNombre());
    }

    @Test
    void agregarAlumno_ReturnsOk() {
        // Arrange
        Map<String, String> payload = Map.of("dni", "123", "nombre", "Alumno", "apellido", "Uno", "celularApoderado", "123456789", "rol", "ALUMNO", "clave", "secreto", "estado", "activo", "genero", "M", "nivel", "Secundaria", "grado", "5to");

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
}