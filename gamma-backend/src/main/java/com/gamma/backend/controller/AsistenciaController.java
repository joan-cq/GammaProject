package com.gamma.backend.controller;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Asistencia;
import com.gamma.backend.model.Curso;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.AsistenciaRepository;
import com.gamma.backend.repository.CursoRepository;
import com.gamma.backend.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AsistenciaController {

    private static final Logger logger = LoggerFactory.getLogger(AsistenciaController.class);

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private LogService logService;

    // RF-05: el docente carga la lista de alumnos del grado con su estado de asistencia del día
    @GetMapping("/asistencia/alumnos")
    public ResponseEntity<?> obtenerAlumnosConAsistencia(
            @RequestParam String codigoGrado,
            @RequestParam String codigoCurso,
            @RequestParam String fecha) {

        LocalDate fechaConsulta = LocalDate.parse(fecha);
        logger.info("Obteniendo asistencia del grado {}, curso {} y fecha {}", codigoGrado, codigoCurso, fechaConsulta);

        List<Alumno> alumnos = alumnoRepository.findByGrado_CodigoGrado(codigoGrado);

        List<Map<String, Object>> alumnosConAsistencia = alumnos.stream().map(alumno -> {
            Asistencia asistencia = asistenciaRepository
                    .findByAlumno_DniAndCurso_CodigoCursoAndFecha(alumno.getDni(), codigoCurso, fechaConsulta)
                    .orElse(null);

            Map<String, Object> item = new HashMap<>();
            item.put("dni", alumno.getDni());
            item.put("nombre", alumno.getNombre());
            item.put("apellido", alumno.getApellido());
            item.put("codigo_grado", alumno.getCodigoGrado());
            item.put("estado", asistencia != null ? asistencia.getEstado() : null);
            item.put("idAsistencia", asistencia != null ? asistencia.getId() : null);
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(alumnosConAsistencia);
    }

    // RF-05: registrar (o actualizar si ya existe) la asistencia de un alumno en una fecha y curso
    @PostMapping("/asistencia/registrar")
    public ResponseEntity<?> registrarAsistencia(@RequestBody Map<String, Object> payload) {
        String dniAlumno = (String) payload.get("dniAlumno");
        String codigoCurso = (String) payload.get("codigoCurso");
        LocalDate fecha = LocalDate.parse((String) payload.get("fecha"));
        String estado = (String) payload.get("estado");

        Alumno alumno = alumnoRepository.findByDni(dniAlumno);
        if (alumno == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No existe un alumno con ese DNI"));
        }
        Curso curso = cursoRepository.findById(codigoCurso).orElse(null);
        if (curso == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No existe el curso indicado"));
        }

        boolean esNuevo = asistenciaRepository
                .findByAlumno_DniAndCurso_CodigoCursoAndFecha(dniAlumno, codigoCurso, fecha)
                .isEmpty();

        Asistencia asistencia = asistenciaRepository
                .findByAlumno_DniAndCurso_CodigoCursoAndFecha(dniAlumno, codigoCurso, fecha)
                .orElseGet(Asistencia::new);

        asistencia.setAlumno(alumno);
        asistencia.setCurso(curso);
        asistencia.setFecha(fecha);
        asistencia.setEstado(estado);

        asistenciaRepository.save(asistencia);

        String categoriaAsistencia = esNuevo ? LogService.CREACION : LogService.MODIFICACION;
        logService.addLog(categoriaAsistencia, "ASISTENCIA", "Se registró la asistencia (" + estado + ") del alumno " + dniAlumno
                + " en el curso " + codigoCurso + " para la fecha " + fecha);

        return ResponseEntity.ok(Map.of("mensaje", "Asistencia registrada correctamente"));
    }

    // RF-02: historial de asistencia del alumno para el Portal de Padres
    @GetMapping("/asistencia/alumno/{dni}")
    public ResponseEntity<List<Map<String, Object>>> obtenerHistorialAsistencia(@PathVariable String dni) {
        logger.info("Obteniendo historial de asistencia para el alumno con DNI {}", dni);
        List<Asistencia> registros = asistenciaRepository.findAllByAlumno_DniOrderByFechaDesc(dni);

        List<Map<String, Object>> respuesta = registros.stream().map(a -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", a.getId());
            item.put("curso", a.getCurso().getNombre());
            item.put("fecha", a.getFecha());
            item.put("estado", a.getEstado());
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }
}
