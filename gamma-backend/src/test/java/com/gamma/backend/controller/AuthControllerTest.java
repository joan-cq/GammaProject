package com.gamma.backend.controller;

import com.gamma.backend.service.auth.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Administrador_ReturnsOk() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("ADMINISTRADOR");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("rol", "ADMINISTRADOR", "mensaje", "Login exitoso"), response.getBody());
    }

    @Test
    void login_ClaveIncorrecta_ReturnsUnauthorized() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "incorrecta");
        when(loginService.autenticar("12345678", "incorrecta")).thenReturn("CLAVE_INCORRECTA");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("error", "Contraseña incorrecta"), response.getBody());
    }

    @Test
    void login_Inactivo_ReturnsForbidden() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("INACTIVO");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(Map.of("error", "Usuario inactivo"), response.getBody());
    }
    @Test
    void login_AnioInactivo_ReturnsForbidden() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("ANIO_INACTIVO");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(Map.of("error", "Usuario no pertenece al año activo"), response.getBody());
    }

    @Test
    void login_NoAniosActivos_ReturnsServiceUnavailable() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("NO_ANIOS_ACTIVOS");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(Map.of("error", "No hay año escolar activo"), response.getBody());
    }

    @Test
    void login_RolDesconocido_ReturnsForbidden() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("ROL_DESCONOCIDO");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(Map.of("error", "Rol no reconocido"), response.getBody());
    }

    @Test
    void login_NoExiste_ReturnsNotFound() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("NO_EXISTE");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Map.of("error", "Usuario no encontrado"), response.getBody());
    }

    @Test
    void login_ErrorInesperado_ReturnsInternalServerError() {
        // Arrange
        Map<String, String> credentials = Map.of("dni", "12345678", "clave", "secreto");
        when(loginService.autenticar("12345678", "secreto")).thenReturn("ERROR_INESPERADO");

        // Act
        ResponseEntity<?> response = authController.login(credentials);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Map.of("error", "Error inesperado"), response.getBody());
    }
}