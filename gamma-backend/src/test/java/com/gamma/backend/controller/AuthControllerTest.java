package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private ProfesorRepository profesorRepository;
    private LogService logService;

    @BeforeEach
    void setUp() throws Exception {
        // Mocks
        authenticationManager = mock(AuthenticationManager.class);
        userRepository = mock(UserRepository.class);
        profesorRepository = mock(ProfesorRepository.class);
        logService = mock(LogService.class);

        // Instancia real del controlador
        authController = new AuthController();

        // Inyectar campos privados con reflexión
        injectPrivateField("authenticationManager", authenticationManager);
        injectPrivateField("userRepository", userRepository);
        injectPrivateField("profesorRepository", profesorRepository);
        injectPrivateField("logService", logService);
        injectPrivateField("jwtSecret", "5502b749aa1ad919050804abbbca2658f30daf2b9b1f6a1cba05642ac9bd8bce762c4c03aa331315caed6a4e269763527457c2a48a94fd8e7de73a45e5e5f598");

    }

    void injectPrivateField(String fieldName, Object value) throws Exception {
        Field field = AuthController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(authController, value);
    }

    @Test
    void testLoginExitosoConProfesorYCurso() {
        String dni = "12345678";
        String clave = "clave123";

        // Datos de usuario
        User user = new User();
        user.setDni(dni);
        user.setRol("PROFESOR");

        // Datos del profesor
        Curso curso = new Curso();
        curso.setCodigoCurso("MAT001");

        Profesor profesor = new Profesor();
        profesor.setDni(dni);
        profesor.setCurso(curso);

        // UserDetails simulado
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(dni)
                .password(clave)
                .roles("PROFESOR")
                .build();

        // Mock autenticación
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByDni(dni)).thenReturn(user);
        when(profesorRepository.findByDni(dni)).thenReturn(profesor);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("dni", dni);
        credentials.put("clave", clave);

        // Ejecutar
        ResponseEntity<?> response = authController.login(credentials);

        // Verificaciones
        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();

        assertNotNull(body);
        assertTrue(body.containsKey("token"));
        assertEquals("PROFESOR", body.get("rol"));
        assertEquals("MAT001", body.get("codigoCurso"));
    }

    @Test
    void testLoginFallidoCredencialesInvalidas() {
        String dni = "00000000";
        String clave = "incorrecto";

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciales inválidas"));

        Map<String, String> credentials = new HashMap<>();
        credentials.put("dni", dni);
        credentials.put("clave", clave);

        ResponseEntity<?> response = authController.login(credentials);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales inválidas", response.getBody());
        verify(logService).addLog(eq("WARN"), contains("Intento de inicio de sesión fallido"));
    }
}
