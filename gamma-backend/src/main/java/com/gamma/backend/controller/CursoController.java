package com.gamma.backend.controller;

import com.gamma.backend.model.Curso;
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
    public ResponseEntity<?> agregarCurso(@RequestBody Map<String, String> payload) {
        String codigoCurso = payload.get("codigoCurso");
        String nombre = payload.get("nombre");

        Curso curso = new Curso();
        curso.setCodigoCurso(codigoCurso);
        curso.setNombre(nombre);
        curso.setEstado("");

        cursoRepository.save(curso);

        return ResponseEntity.ok(Map.of("mensaje", "Curso agregado con éxito"));
    }

    @PutMapping("/curso/update")
    public ResponseEntity<Curso> actualizarCurso(@RequestBody Curso curso) {
        Curso cursoExistente = cursoRepository.findById(curso.getCodigoCurso()).orElse(null);

        if (cursoExistente != null) {
            cursoExistente.setEstado("ACTIVO");

            Curso cursoActualizado = cursoRepository.save(cursoExistente);
            return ResponseEntity.ok(cursoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/curso/delete/{codigoCurso}")
    public ResponseEntity<?> eliminarCurso(@PathVariable String codigoCurso) {
        if (cursoRepository.existsById(codigoCurso)) {
            cursoRepository.deleteById(codigoCurso);
            return ResponseEntity.ok(Map.of("mensaje", "Curso eliminado con éxito"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
