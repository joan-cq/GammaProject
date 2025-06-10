package com.gamma.backend.controller;

import com.gamma.backend.service.auth.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
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
                logger.warn("Intento de inicio de sesión fallido para el usuario con DNI: {} - Contraseña incorrecta", dni);
                response = ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
                break;
            case "INACTIVO":
                logger.warn("Intento de inicio de sesión fallido para el usuario con DNI: {} - Usuario inactivo", dni);
                response = ResponseEntity.status(403).body(Map.of("error", "Usuario inactivo"));
                break;
            case "ROL_DESCONOCIDO":
                logger.error("Intento de inicio de sesión fallido para el usuario con DNI: {} - Rol desconocido", dni);
                response = ResponseEntity.status(403).body(Map.of("error", "Rol no reconocido"));
                break;
            case "NO_EXISTE":
                logger.warn("Intento de inicio de sesión fallido para el usuario con DNI: {} - Usuario no encontrado", dni);
                response = ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
                break;
            default:
                logger.error("Error inesperado durante el inicio de sesión para el usuario con DNI: {}", dni);
                response = ResponseEntity.status(500).body(Map.of("error", "Error inesperado"));
        }
        return response;
    }
}
