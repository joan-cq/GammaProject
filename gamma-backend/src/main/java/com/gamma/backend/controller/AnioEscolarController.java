package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.AnioEscolarRepository;
import com.gamma.backend.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anioescolar")
public class AnioEscolarController {

    @Autowired
    private AnioEscolarRepository anioEscolarRepository;

    @Autowired
    private LogService logService;

    private static final Logger logger = LoggerFactory.getLogger(AnioEscolarController.class);

    // Lista todos los años escolares
    @GetMapping("/list")
    public ResponseEntity<List<AnioEscolar>> listarAniosEscolares() {
        List<AnioEscolar> lista = anioEscolarRepository.findAll();
        logService.addLog("INFO", "Se ha listado los años escolares.");
        return ResponseEntity.ok(lista);
    }

    // Cambia el estado del año escolar
    @PutMapping("/update")
    public ResponseEntity<?> actualizarEstado(@RequestBody Map<String, Object> payload) {
        Long id = Long.parseLong(payload.get("id").toString());
        String nuevoEstado = payload.get("estado").toString();

        AnioEscolar anio = anioEscolarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Año escolar no encontrado con ID: " + id));

        anio.setEstado(nuevoEstado);
        anioEscolarRepository.save(anio);
        logger.info("Año escolar {} actualizado a estado {}", anio.getAnio(), nuevoEstado);
        logService.addLog("INFO", "Se ha actualizado el estado del año escolar " + anio.getAnio() + " a " + nuevoEstado);

        return ResponseEntity.ok(Map.of("mensaje", "Año escolar actualizado con éxito"));
    }
}
