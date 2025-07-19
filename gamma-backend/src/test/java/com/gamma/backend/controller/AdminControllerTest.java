package com.gamma.backend.controller;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.UserRepository;
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

class AdminControllerTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnioEscolarService anioEscolarService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void listarAdministradores_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Administrador admin1 = new Administrador();
        admin1.setDni("123");
        admin1.setNombre("Admin");
        admin1.setApellido("Uno");
        User user1 = new User();
        user1.setDni("123");
        user1.setRol("ADMINISTRADOR");
        admin1.setUser(user1);
        admin1.setAnioEscolar(anioActivo);

        when(anioEscolarService.obtenerAniosActivos()).thenReturn(List.of(anioActivo));
        when(administradorRepository.findByAnioEscolarIn(List.of(anioActivo))).thenReturn(List.of(admin1));

        // Act
        ResponseEntity<List<Administrador>> response = adminController.listarAdministradores();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ADMINISTRADOR", response.getBody().get(0).getRol());

    }

    @SuppressWarnings("null")
    @Test
    void actualizarAdministrador_ReturnsOk() {
        // Arrange
        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setNombre("Admin");
        admin.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setRol("ADMINISTRADOR");
        admin.setUser(user);

        when(administradorRepository.findById("123")).thenReturn(Optional.of(admin));
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        // Act
        ResponseEntity<Administrador> response = adminController.actualizarAdministrador(admin);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin", response.getBody().getNombre());
    }

    @Test
    void agregarAdministrador_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Map<String, String> payload = Map.of("dni", "123", "nombre", "Admin", "apellido", "Uno", "celular", "123456789", "clave", "secreto");

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));

        // Act
        ResponseEntity<?> response = adminController.agregarAdministrador(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Administrador agregado con éxito"), response.getBody());
    }

    @Test
    void eliminarAdministrador_ReturnsOk() {
        // Arrange
        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setNombre("Admin");
        admin.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setRol("ADMINISTRADOR");
        admin.setUser(user);

        when(administradorRepository.findById("123")).thenReturn(Optional.of(admin));

        // Act
        ResponseEntity<?> response = adminController.eliminarAdministrador("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Administrador eliminado con éxito"), response.getBody());
    }

    @Test
    void actualizarPassword_ReturnsOk() {
        // Arrange
        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setNombre("Admin");
        admin.setApellido("Uno");
        User user = new User();
        user.setDni("123");
        user.setClave("secreto");
        admin.setUser(user);

        Map<String, String> payload = Map.of("nuevaClave", "nuevaSecreto");

        when(administradorRepository.findById("123")).thenReturn(Optional.of(admin));

        // Act
        ResponseEntity<?> response = adminController.actualizarPassword("123", payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Contraseña actualizada con éxito"), response.getBody());
    }
}