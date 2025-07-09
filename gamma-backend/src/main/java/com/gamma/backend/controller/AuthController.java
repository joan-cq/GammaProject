package com.gamma.backend.controller;

import com.gamma.backend.service.auth.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String dni = credentials.get("dni");
        String clave = credentials.get("clave");

        String resultado = loginService.autenticar(dni, clave);
        logger.info("Intento de inicio de sesión para el usuario con DNI: {}", dni);

        ResponseEntity<?> response;
        switch (resultado) {
            case "ADMINISTRADOR", "PROFESOR", "ALUMNO":
                logger.info("Inicio de sesión exitoso para el usuario con DNI: {} y rol: {}", dni, resultado);
                response = ResponseEntity.ok(Map.of("rol", resultado, "mensaje", "Login exitoso"));
                break;
            case "CLAVE_INCORRECTA":
                logger.warn("Contraseña incorrecta para el usuario con DNI: {}", dni);
                response = ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
                break;
            case "INACTIVO":
                logger.warn("Usuario inactivo con DNI: {}", dni);
                response = ResponseEntity.status(403).body(Map.of("error", "Usuario inactivo"));
                break;
            case "ANIO_INACTIVO":
                logger.warn("Usuario con DNI: {} no pertenece al año escolar activo", dni);
                response = ResponseEntity.status(403).body(Map.of("error", "Usuario no pertenece al año activo"));
                break;
            case "NO_ANIOS_ACTIVOS":
                logger.error("No hay año escolar activo en el sistema al intentar login de DNI: {}", dni);
                response = ResponseEntity.status(503).body(Map.of("error", "No hay año escolar activo"));
                break;
            case "ROL_DESCONOCIDO":
                logger.error("Rol desconocido para el usuario con DNI: {}", dni);
                response = ResponseEntity.status(403).body(Map.of("error", "Rol no reconocido"));
                break;
            case "NO_EXISTE":
                logger.warn("Usuario no encontrado con DNI: {}", dni);
                response = ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
                break;
            default:
                logger.error("Error inesperado durante el inicio de sesión para el usuario con DNI: {}", dni);
                response = ResponseEntity.status(500).body(Map.of("error", "Error inesperado"));
        }
        return response;
    }
}
