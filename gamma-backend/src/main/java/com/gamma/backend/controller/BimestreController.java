package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
