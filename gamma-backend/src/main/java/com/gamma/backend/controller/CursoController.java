package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Estado;
import com.gamma.backend.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/curso/list")
    public ResponseEntity<List<Curso>> listarCursos() {
        List<Curso> cursos = cursoRepository.findAll();
        return ResponseEntity.ok(cursos);
    }

    @PostMapping("/curso/add")
    public ResponseEntity<?> agregarCurso(@RequestBody Curso curso) {
        curso.setEstado(Estado.ACTIVO);
        cursoRepository.save(curso);
        return ResponseEntity.ok(Map.of("mensaje", "Curso agregado con éxito"));
    }

    @PutMapping("/curso/update/{codigoCurso}")
    public ResponseEntity<?> actualizarCurso(@PathVariable String codigoCurso, @RequestBody Curso cursoDetails) {
        Curso cursoExistente = cursoRepository.findById(codigoCurso).orElse(null);
        if (cursoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        cursoExistente.setNombre(cursoDetails.getNombre());
        cursoExistente.setEstado(cursoDetails.getEstado());
        cursoRepository.save(cursoExistente);
        return ResponseEntity.ok(Map.of("mensaje", "Curso actualizado con éxito"));
    }

    @PutMapping("/curso/toggle/{codigoCurso}")
    public ResponseEntity<?> toggleCurso(@PathVariable String codigoCurso) {
        Curso cursoExistente = cursoRepository.findById(codigoCurso).orElse(null);
        if (cursoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        Estado nuevoEstado = cursoExistente.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO;
        cursoExistente.setEstado(nuevoEstado);
        cursoRepository.save(cursoExistente);
        return ResponseEntity.ok(Map.of("mensaje", "Estado del curso actualizado con éxito"));
    }
}
