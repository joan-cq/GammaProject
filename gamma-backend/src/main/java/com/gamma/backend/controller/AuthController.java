package com.gamma.backend.controller;

import com.gamma.backend.service.auth.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String dni = credentials.get("dni");
        String clave = credentials.get("clave");

        String resultado = loginService.autenticar(dni, clave);

        return switch (resultado) {
            case "ADMINISTRADOR", "PROFESOR", "ALUMNO" -> 
                ResponseEntity.ok(Map.of("rol", resultado, "mensaje", "Login exitoso"));
            case "CLAVE_INCORRECTA" -> 
                ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
            case "INACTIVO" -> 
                ResponseEntity.status(403).body(Map.of("error", "Usuario inactivo"));
            case "ROL_DESCONOCIDO" -> 
                ResponseEntity.status(403).body(Map.of("error", "Rol no reconocido"));
            case "NO_EXISTE" -> 
                ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            default -> 
                ResponseEntity.status(500).body(Map.of("error", "Error inesperado"));
        };
    }
}
