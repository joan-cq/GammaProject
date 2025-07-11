package com.gamma.backend.controller;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bimestre")

public class BimestreController {

    private final AnioEscolarService anioEscolarService;
    private final BimestreRepository bimestreRepository;

    @Autowired
    public BimestreController(AnioEscolarService anioEscolarService, BimestreRepository bimestreRepository) {
        this.anioEscolarService = anioEscolarService;
        this.bimestreRepository = bimestreRepository;
    }

    @GetMapping("/activos")
    public List<Bimestre> listarBimestresActivos() {
        AnioEscolar anioActivo = anioEscolarService.obtenerAnioActivo().orElse(null);
        return bimestreRepository.findByAnioEscolar(anioActivo);
    }
}
