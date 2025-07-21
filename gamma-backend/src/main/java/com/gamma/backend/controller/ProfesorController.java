package com.gamma.backend.controller;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.LogService;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gamma.backend.model.Curso;
import com.gamma.backend.repository.CursoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProfesorController {
    private static final Logger logger = LoggerFactory.getLogger(ProfesorController.class);

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnioEscolarService anioEscolarService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogService logService;

    @GetMapping("/profesor/list")
    public ResponseEntity<List<Profesor>> listarProfesores() {
        List<Profesor> profesores = profesorRepository.findAll();
        for (Profesor profesor : profesores) {
            if (profesor.getUser() != null && profesor.getCurso() != null && profesor.getAnioEscolar() != null) {
                profesor.setCodigoCurso(profesor.getCurso().getNombre());
                profesor.setRol(profesor.getUser().getRol());
                profesor.setClave(profesor.getUser().getClave());
                profesor.setAnio(profesor.getAnioEscolar().getAnio());
            }
        }
        logger.info("Listado de profesores solicitado.");
        logService.addLog("INFO", "Se ha listado los profesores.");
        return ResponseEntity.ok(profesores);
    }

    @PutMapping("/profesor/update")
    public ResponseEntity<Profesor> actualizarProfesor(@RequestBody Map<String, Object> payload) {
        String dni = (String) payload.get("dni");
        String nombre = (String) payload.get("nombre");
        String apellido = (String) payload.get("apellido");
        String celular = (String) payload.get("celular");
        String cursoId = (String) payload.get("curso");
        String estado = (String) payload.get("estado");
        String rol = (String) payload.get("rol");

        // Obtener el profesor existente de la base de datos
        Profesor profesorExistente = profesorRepository.findById(dni).orElse(null);

        if (profesorExistente != null) {
            // Actualizar los atributos del profesor existente
            profesorExistente.setNombre(nombre);
            profesorExistente.setApellido(apellido);
            profesorExistente.setCelular(celular);
            if (cursoId != null) {
                Curso curso = cursoRepository.findById(cursoId).orElse(null);
                profesorExistente.setCurso(curso);
            }
            profesorExistente.setEstado(estado);
            if (rol != null && profesorExistente.getUser() != null) {
                profesorExistente.getUser().setRol(rol);
                userRepository.save(profesorExistente.getUser());
            }

            Profesor profesorActualizado = profesorRepository.save(profesorExistente);
            logger.info("Profesor con DNI {} actualizado.", dni);
            logService.addLog("INFO", "Se ha actualizado el profesor con DNI: " + dni);
            return ResponseEntity.ok(profesorActualizado);
        } else {
            logger.warn("Intento de actualizar profesor con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profesor/add")
    public ResponseEntity<?> agregarProfesor(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String nombre = payload.get("nombre");
        String apellido = payload.get("apellido");
        String celular = payload.get("celular");
        String codigoCurso = payload.get("curso");
        String clave = payload.get("clave");

        if (userRepository.existsById(dni)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con el DNI " + dni));
        }

        User user = new User();
        user.setDni(dni);
        user.setClave(passwordEncoder.encode(clave));
        user.setRol("PROFESOR");
        userRepository.save(user);

        Profesor profesor = new Profesor();
        profesor.setDni(dni);
        profesor.setNombre(nombre);
        profesor.setApellido(apellido);
        profesor.setCelular(celular);
        profesor.setEstado("ACTIVO");

        Curso curso = cursoRepository.findById(codigoCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con código: " + codigoCurso));
        profesor.setCurso(curso);

        AnioEscolar anioEscolar = anioEscolarService.obtenerAnioActivo()
                .orElseThrow(() -> new RuntimeException("No hay año escolar activo"));
        profesor.setAnioEscolar(anioEscolar);

        profesorRepository.save(profesor);
        logger.info("Profesor con DNI {} agregado.", dni);
        logService.addLog("INFO", "Se ha agregado un nuevo profesor con DNI: " + dni);

        return ResponseEntity.ok(Map.of("mensaje", "Profesor agregado con éxito"));
    }

    @DeleteMapping("/profesor/delete/{dni}")
    public ResponseEntity<?> eliminarProfesor(@PathVariable String dni) {
        // Primero, buscar el profesor por DNI
        Profesor profesor = profesorRepository.findById(dni).orElse(null);

        if (profesor != null) {
            // Eliminar el profesor de la tabla Profesor
            profesorRepository.deleteById(dni);

            // Obtener el usuario asociado al profesor
            User user = profesor.getUser();

            if (user != null) {
                // Eliminar el usuario de la tabla User
                userRepository.deleteById(user.getDni());
            }

            logger.info("Profesor con DNI {} eliminado.", dni);
            logService.addLog("INFO", "Se ha eliminado el profesor con DNI: " + dni);
            return ResponseEntity.ok(Map.of("mensaje", "Profesor eliminado con éxito"));
        } else {
            logger.warn("Intento de eliminar profesor con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profesor/updatePassword/{dni}")
    public ResponseEntity<?> actualizarPassword(@PathVariable String dni, @RequestBody Map<String, String> payload) {
        // Obtener la nueva contraseña del payload
        String nuevaClave = payload.get("nuevaClave");

        // Obtener el profesor existente de la base de datos
        Profesor profesorExistente = profesorRepository.findById(dni).orElse(null);

        if (profesorExistente != null) {
            // Obtener el usuario asociado al profesor
            User user = profesorExistente.getUser();

            if (user != null) {
                // Actualizar la contraseña del usuario
                user.setClave(passwordEncoder.encode(nuevaClave));
                userRepository.save(user);

                return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito"));
            } else {
                logger.warn("Intento de actualizar contraseña del profesor con DNI {} que no existe.", dni);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Intento de actualizar contraseña del profesor con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }
    @Autowired
    private com.gamma.backend.service.modelservice.ProfesorService profesorService;

    @GetMapping("/profesor/codigo_curso")
    public ResponseEntity<?> obtenerCodigoCurso(@RequestParam String dni) {
        String codigoCurso = profesorService.obtenerCodigoCursoPorDni(dni);
        if (codigoCurso != null) {
            return ResponseEntity.ok(Map.of("codigo_curso", codigoCurso));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}