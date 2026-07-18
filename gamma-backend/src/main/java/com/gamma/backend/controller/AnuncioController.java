package com.gamma.backend.controller;

import com.gamma.backend.model.Anuncio;
import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.Administrador;
import com.gamma.backend.repository.AnuncioRepository;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    private static final Logger logger = LoggerFactory.getLogger(AnuncioController.class);

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private LogService logService;

    // RF-03: el Portal de Padres (y el panel del docente) listan los anuncios publicados
    @GetMapping
    public ResponseEntity<List<Anuncio>> listarAnuncios() {
        logger.info("Listando anuncios institucionales");
        return ResponseEntity.ok(anuncioRepository.findAllByOrderByFechaPublicacionDesc());
    }

    // RF-06: el docente publica un anuncio
    @PostMapping("/add")
    public ResponseEntity<?> publicarAnuncio(@RequestBody Map<String, String> payload) {
        String titulo = payload.get("titulo");
        String contenido = payload.get("contenido");
        String dniAutor = payload.get("dniAutor");

        if (titulo == null || titulo.isBlank() || contenido == null || contenido.isBlank() || dniAutor == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Título, contenido y autor son obligatorios"));
        }

        String nombreAutor = resolverNombreAutor(dniAutor);

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setContenido(contenido);
        anuncio.setDniAutor(dniAutor);
        anuncio.setNombreAutor(nombreAutor);

        anuncioRepository.save(anuncio);
        logService.addLog(LogService.CREACION, "ANUNCIO", "Se publicó el anuncio \"" + titulo + "\" (autor DNI: " + dniAutor + ")");

        return ResponseEntity.ok(Map.of("mensaje", "Anuncio publicado correctamente"));
    }

    // RF-06: el docente/administrador puede eliminar un anuncio propio
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarAnuncio(@PathVariable Long id) {
        if (!anuncioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        anuncioRepository.deleteById(id);
        logService.addLog(LogService.ELIMINACION, "ANUNCIO", "Se eliminó el anuncio con ID: " + id);
        return ResponseEntity.ok(Map.of("mensaje", "Anuncio eliminado correctamente"));
    }

    private String resolverNombreAutor(String dni) {
        Profesor profesor = profesorRepository.findByDni(dni);
        if (profesor != null) {
            return profesor.getNombre() + " " + profesor.getApellido();
        }
        Administrador administrador = administradorRepository.findByDni(dni);
        if (administrador != null) {
            return administrador.getNombre() + " " + administrador.getApellido();
        }
        return "Colegio Gamma";
    }
}
