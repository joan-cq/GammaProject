package com.gamma.backend.controller;

import com.gamma.backend.model.User;
import com.gamma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String dni = authentication.getName();
        User user = userRepository.findByDni(dni);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("dni", user.getDni());
        userDetails.put("rol", user.getRol());

        return ResponseEntity.ok(userDetails);
    }
}