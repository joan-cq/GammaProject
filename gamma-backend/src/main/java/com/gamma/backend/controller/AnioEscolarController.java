package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.AnioEscolarRepository;

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

    private static final Logger logger = LoggerFactory.getLogger(AnioEscolarController.class);

    // Lista todos los años escolares
    @GetMapping("/list")
    public ResponseEntity<List<AnioEscolar>> listarAniosEscolares() {
        List<AnioEscolar> lista = anioEscolarRepository.findAll();
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

        return ResponseEntity.ok(Map.of("mensaje", "Año escolar actualizado con éxito"));
    }
}
