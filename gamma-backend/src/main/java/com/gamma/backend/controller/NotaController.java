package com.gamma.backend.controller;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Nota;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.NotaRepository;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.service.modelservice.NotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class NotaController {

    private static final Logger logger = LoggerFactory.getLogger(NotaController.class);

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private BimestreRepository bimestreRepository;

    @Autowired
    private NotaService notaService;

    @GetMapping("/notas/alumnos")
    public ResponseEntity<?> obtenerAlumnosPorGradoBimestreCurso(
            @RequestParam String codigoGrado,
            @RequestParam Integer idBimestre,
            @RequestParam String codigoCurso) {

        logger.info("Obteniendo alumnos para el grado {}, bimestre {} y curso {}", codigoGrado, idBimestre, codigoCurso);

        List<Alumno> alumnos = alumnoRepository.findByCodigoGrado(codigoGrado);

        List<Map<String, Object>> alumnosConNotas = alumnos.stream().map(alumno -> {
            Nota nota = notaRepository.findByAlumno_DniAndCurso_CodigoCursoAndBimestre_Id(alumno.getDni(), codigoCurso, idBimestre);
            Map<String, Object> alumnoConNota = Map.of(
                    "dni", alumno.getDni(),
                    "nombre", alumno.getNombre(),
                    "apellido", alumno.getApellido(),
                    "codigo_grado", alumno.getCodigoGrado(),
                    "nota", nota != null ? nota.getNota() : null
            );
            return alumnoConNota;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(alumnosConNotas);
    }

    @PostMapping("/nota/add")
    public ResponseEntity<?> agregarNota(@RequestBody Map<String, Object> payload) {
        String dniAlumno = (String) payload.get("dniAlumno");
        String codigoCurso = (String) payload.get("codigoCurso");
        Integer idBimestre = Integer.parseInt((String) payload.get("idBimestre"));
        BigDecimal notaValue = new BigDecimal((String) payload.get("nota"));

        Nota nota = new Nota();
        Alumno alumno = alumnoRepository.findByDni(dniAlumno);
        nota.setAlumno(alumno);
        //nota.setCurso(cursoRepository.findById(codigoCurso).orElse(null));
        Bimestre bimestre = bimestreRepository.findById(Long.valueOf(idBimestre)).orElse(null);
        nota.setBimestre(bimestre);
        nota.setNota(notaValue.doubleValue());

        notaService.guardarNota(nota);

        return ResponseEntity.ok(Map.of("mensaje", "Nota agregada correctamente"));
    }

    @PutMapping("/nota/update")
    public ResponseEntity<?> actualizarNota(@RequestBody Map<String, Object> payload) {
        Integer idNota = (Integer) payload.get("idNota");
        BigDecimal notaValue = new BigDecimal((String) payload.get("nota"));

        Nota nota = notaRepository.findById(Long.valueOf(idNota)).orElse(null);
        nota.setNota(notaValue.doubleValue());

        notaService.actualizarNota(nota);

        return ResponseEntity.ok(Map.of("mensaje", "Nota actualizada correctamente"));
    }
}