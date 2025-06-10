package com.gamma.backend.controller;

import com.gamma.backend.model.Nota;
import com.gamma.backend.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @GetMapping("/nota/list")
    public ResponseEntity<List<Nota>> listarNotas() {
        List<Nota> notas = notaRepository.findAll();
        return ResponseEntity.ok(notas);
    }

    @PostMapping("/nota/add")
    public ResponseEntity<Nota> agregarNota(@RequestBody Nota nota) {
        Nota nuevaNota = notaRepository.save(nota);
        return ResponseEntity.ok(nuevaNota);
    }

    @PutMapping("/nota/update/{id}")
    public ResponseEntity<Nota> actualizarNota(@PathVariable Long id, @RequestBody Nota nota) {
        Nota notaExistente = notaRepository.findById(id).orElse(null);
        if (notaExistente != null) {
            nota.setId(id);
            Nota notaActualizada = notaRepository.save(nota);
            return ResponseEntity.ok(notaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/nota/delete/{id}")
    public ResponseEntity<?> eliminarNota(@PathVariable Long id) {
        Nota notaExistente = notaRepository.findById(id).orElse(null);
        if (notaExistente != null) {
            notaRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Nota eliminada con éxito"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
