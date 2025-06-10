package com.gamma.backend.service.auth;

import com.gamma.backend.model.User;
import com.gamma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final Map<String, RoleAuthStrategy> estrategias;

    @Autowired
    public LoginService(UserRepository userRepository, List<RoleAuthStrategy> strategyList) {
        this.userRepository = userRepository;

        // Mapeo cada estrategia según su rol, ej: "ADMINISTRADOR" -> AdminAuthStrategy
        this.estrategias = strategyList.stream()
                .collect(Collectors.toMap(RoleAuthStrategy::getRol, e -> e));
    }

    public String autenticar(String dni, String clave) {
        User usuario = userRepository.findByDni(dni);
        if (usuario == null) {
            return "NO_EXISTE";
        }

        if (!usuario.getClave().equals(clave)) {
            return "CLAVE_INCORRECTA";
        }

        RoleAuthStrategy strategy = estrategias.get(usuario.getRol());
        if (strategy == null) {
            return "ROL_DESCONOCIDO";
        }

        boolean activo = strategy.estaActivo(dni);
        return activo ? usuario.getRol() : "INACTIVO";
    }
}
