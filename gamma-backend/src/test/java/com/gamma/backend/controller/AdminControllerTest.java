package com.gamma.backend.controller;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.LogService;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock private AdministradorRepository administradorRepository;
    @Mock private UserRepository userRepository;
    @Mock private AnioEscolarService anioEscolarService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private LogService logService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarAdministradores_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        User user = new User();
        user.setDni("123");
        user.setRol("ADMINISTRADOR");
        user.setClave("claveCodificada");

        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setNombre("Admin");
        admin.setApellido("Uno");
        admin.setUser(user);
        admin.setAnioEscolar(anioActivo);

        when(anioEscolarService.obtenerAniosActivos()).thenReturn(List.of(anioActivo));
        when(administradorRepository.findByAnioEscolarIn(List.of(anioActivo))).thenReturn(List.of(admin));

        // Act
        ResponseEntity<List<Administrador>> response = adminController.listarAdministradores();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ADMINISTRADOR", response.getBody().get(0).getRol());
    }

    @Test
    void actualizarAdministrador_ReturnsOk() {
        // Arrange
        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setNombre("Admin");
        admin.setApellido("Uno");
        admin.setCelular("987654321");
        admin.setEstado("ACTIVO");

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
        verify(userRepository).save(any(User.class));
    }

    @Test
    void agregarAdministrador_ReturnsOk() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Map<String, String> payload = Map.of(
            "dni", "123",
            "nombre", "Admin",
            "apellido", "Uno",
            "celular", "123456789",
            "clave", "secreto"
        );

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(passwordEncoder.encode("secreto")).thenReturn("claveCodificada");

        // Act
        ResponseEntity<?> response = adminController.agregarAdministrador(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Administrador agregado con éxito"), response.getBody());
        verify(userRepository).save(any(User.class));
        verify(administradorRepository).save(any(Administrador.class));
    }

    @Test
    void eliminarAdministrador_ReturnsOk() {
        // Arrange
        Administrador admin = new Administrador();
        admin.setDni("123");

        User user = new User();
        user.setDni("123");
        admin.setUser(user);

        when(administradorRepository.findById("123")).thenReturn(Optional.of(admin));

        // Act
        ResponseEntity<?> response = adminController.eliminarAdministrador("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Administrador eliminado con éxito"), response.getBody());
        verify(administradorRepository).deleteById("123");
        verify(userRepository).deleteById("123");
    }

    @Test
    void actualizarPassword_ReturnsOk() {
        // Arrange
        User user = new User();
        user.setDni("123");
        user.setClave("claveAntigua");

        Administrador admin = new Administrador();
        admin.setDni("123");
        admin.setUser(user);

        Map<String, String> payload = Map.of("nuevaClave", "nuevaClave123");

        when(administradorRepository.findById("123")).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode("nuevaClave123")).thenReturn("claveNuevaCodificada");

        // Act
        ResponseEntity<?> response = adminController.actualizarPassword("123", payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Contraseña actualizada con éxito"), response.getBody());
        verify(userRepository).save(user);
    }
}
