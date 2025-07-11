package com.gamma.backend.controller;

import com.gamma.backend.model.Nota;
import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.model.Curso;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.gamma.backend.service.modelservice.NotaService;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.repository.CursoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/nota")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @GetMapping
    public List<Nota> obtenerNotas(@RequestParam String grado, @RequestParam Long bimestre, @RequestParam String dniProfesor) {
        return notaService.obtenerNotasPorGradoYBimestre(grado, bimestre, dniProfesor);
    }

    @PostMapping("/add")
    public ResponseEntity<String> registrarNota(@RequestBody Nota nota) {
        notaService.guardarNota(nota);
        return ResponseEntity.ok("Nota registrada");
    }

    @PutMapping("/update")
    public ResponseEntity<String> actualizarNota(@RequestBody Nota nota) {
        notaService.actualizarNota(nota);
        return ResponseEntity.ok("Nota actualizada");
    }
}