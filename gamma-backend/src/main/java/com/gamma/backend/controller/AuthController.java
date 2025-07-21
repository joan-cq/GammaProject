package com.gamma.backend.controller;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String dni = credentials.get("dni");
        String clave = credentials.get("clave");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dni, clave));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByDni(userDetails.getUsername());

        String token = Jwts.builder()
                .setSubject(user.getDni())
                .claim("rol", user.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("rol", user.getRol());

        if ("PROFESOR".equals(user.getRol())) {
            Profesor profesor = profesorRepository.findByDni(user.getDni());
            if (profesor != null && profesor.getCurso() != null) {
                response.put("codigoCurso", profesor.getCurso().getCodigoCurso());
            }
        }

        return ResponseEntity.ok(response);
    }
}
