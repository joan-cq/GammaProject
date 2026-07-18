package com.gamma.backend.service.auth;

import com.gamma.backend.model.User;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final Map<String, RoleAuthStrategy> estrategias;
    private final AnioEscolarService anioEscolarService;

    @Autowired
    public LoginService(UserRepository userRepository,
                        List<RoleAuthStrategy> strategyList,
                        AnioEscolarService anioEscolarService) {
        this.userRepository = userRepository;
        this.estrategias = strategyList.stream()
                .collect(Collectors.toMap(RoleAuthStrategy::getRol, e -> e));
        this.anioEscolarService = anioEscolarService;
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

        List<AnioEscolar> aniosActivos = anioEscolarService.obtenerAniosActivos();
        if (aniosActivos.isEmpty()) {
            return "NO_AÑOS_ACTIVOS";
        }

        boolean perteneceAAlguno = aniosActivos.stream()
                .anyMatch(anio -> strategy.estaActivo(dni, anio.getId()));

        return perteneceAAlguno ? usuario.getRol() : "AÑO_INACTIVO";
    }
}
