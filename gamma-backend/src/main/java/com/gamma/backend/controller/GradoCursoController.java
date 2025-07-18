package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Curso;
import com.gamma.backend.model.Grado;
import com.gamma.backend.model.GradoCurso;
import com.gamma.backend.repository.GradoCursoRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grado_curso")
public class GradoCursoController {

    private static final Logger logger = LoggerFactory.getLogger(GradoCursoController.class);

    private final GradoCursoRepository gradoCursoRepository;
    private final AnioEscolarService anioEscolarService;

    @Autowired
    public GradoCursoController(GradoCursoRepository gradoCursoRepository, AnioEscolarService anioEscolarService) {
        this.gradoCursoRepository = gradoCursoRepository;
        this.anioEscolarService = anioEscolarService;
    }

    @GetMapping("/filtrar/{codigoCurso}")
    public List<Grado> listarGradosPorCurso(@PathVariable String codigoCurso) {
        logger.info("Entrando al método listarGradosPorCurso con codigoCurso: {}", codigoCurso);
        AnioEscolar anioActivo = anioEscolarService.obtenerAnioActivo().orElse(null);
        logger.info("Anio activo: {}", anioActivo);
        List<GradoCurso> relaciones = gradoCursoRepository.findByCurso_CodigoCursoAndAnioEscolar_IdAndEstado(
            codigoCurso, anioActivo.getId(), "ACTIVO"
        );
        logger.info("Relaciones encontradas: {}", relaciones);
        logger.info("Saliendo del método listarGradosPorCurso");
        return relaciones.stream().map(GradoCurso::getGrado).toList();
    }
    @GetMapping("/cursos/{codigoGrado}")
    public List<Curso> listarCursosPorGrado(@PathVariable String codigoGrado) {
        logger.info("Entrando al método listarCursosPorGrado con codigoGrado: {}", codigoGrado);
        AnioEscolar anioActivo = anioEscolarService.obtenerAnioActivo().orElse(null);
        if (anioActivo == null) {
            logger.warn("No hay año escolar activo.");
            return List.of();
        }
        logger.info("Anio activo: {}", anioActivo.getId());
        List<GradoCurso> relaciones = gradoCursoRepository.findByGrado_CodigoGradoAndAnioEscolar_IdAndEstado(
            codigoGrado, anioActivo.getId(), "ACTIVO"
        );
        logger.info("Relaciones encontradas: {}", relaciones.size());
        return relaciones.stream().map(GradoCurso::getCurso).toList();
    }
}
