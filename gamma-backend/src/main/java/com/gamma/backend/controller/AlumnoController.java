package com.gamma.backend.controller;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.Grado;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gamma.backend.model.Curso;
import com.gamma.backend.repository.CursoRepository;
import com.gamma.backend.repository.GradoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AlumnoController {
    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private GradoRepository gradoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnioEscolarService anioEscolarService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/alumno/list")
    public ResponseEntity<List<Alumno>> listarAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        for (Alumno alumno : alumnos) {
            if (alumno.getUser() != null && alumno.getGrado() != null && alumno.getAnioEscolar() != null) {
                alumno.setCodigoGrado(alumno.getGrado().getCodigoGrado());
                alumno.setRol(alumno.getUser().getRol());
                alumno.setClave(alumno.getUser().getClave());
                alumno.setAnio(alumno.getAnioEscolar().getAnio());
            }
        }
        logger.info("Listado de alumnos solicitado.");
        return ResponseEntity.ok(alumnos);
    }

    @PutMapping("/alumno/update")
    public ResponseEntity<Alumno> actualizarAlumno(@RequestBody Map<String, Object> payload) {
        String dni = (String) payload.get("dni");
        String nombre = (String) payload.get("nombre");
        String apellido = (String) payload.get("apellido");
        String celular = (String) payload.get("celularApoderado");
        String gradoId = (String) payload.get("codigoGrado");
        String estado = (String) payload.get("estado");
        String rol = (String) payload.get("rol");

        // Obtener el alumno existente de la base de datos
        Alumno alumnoExistente = alumnoRepository.findById(dni).orElse(null);

        if (alumnoExistente != null) {
            // Actualizar los atributos del alumno existente
            alumnoExistente.setNombre(nombre);
            alumnoExistente.setApellido(apellido);
            alumnoExistente.setCelularApoderado(celular);
            if (gradoId != null) {
                Grado grado = gradoRepository.findById(gradoId).orElse(null);
                alumnoExistente.setGrado(grado);
            }
            alumnoExistente.setEstado(estado);
            if (rol != null && alumnoExistente.getUser() != null) {
                alumnoExistente.getUser().setRol(rol);
                userRepository.save(alumnoExistente.getUser());
            }

            Alumno alumnoActualizado = alumnoRepository.save(alumnoExistente);
            logger.info("Alumno con DNI {} actualizado.", dni);
            return ResponseEntity.ok(alumnoActualizado);
        } else {
            logger.warn("Intento de actualizar alumno con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/alumno/add")
    public ResponseEntity<?> agregarAlumno(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String nombre = payload.get("nombre");
        String apellido = payload.get("apellido");
        String celular = payload.get("celularApoderado");
        String genero = payload.get("genero");
        String codigoGrado = payload.get("codigoGrado");
        String clave = payload.get("clave");

        if (userRepository.existsById(dni)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con el DNI " + dni));
        }

        User user = new User();
        user.setDni(dni);
        user.setClave(passwordEncoder.encode(clave));
        user.setRol("ALUMNO");
        userRepository.save(user);

        Alumno alumno = new Alumno();
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setCelularApoderado(celular);
        alumno.setGenero(genero);

        Grado grado = gradoRepository.findById(codigoGrado)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado con código: " + codigoGrado));
        alumno.setGrado(grado);

        alumno.setEstado("ACTIVO");

        AnioEscolar anioEscolar = anioEscolarService.obtenerAnioActivo()
                .orElseThrow(() -> new RuntimeException("No hay año escolar activo"));
        alumno.setAnioEscolar(anioEscolar);

        alumnoRepository.save(alumno);
        logger.info("Alumno con DNI {} agregado.", dni);

        return ResponseEntity.ok(Map.of("mensaje", "Alumno agregado con éxito"));
    }

    @DeleteMapping("/alumno/delete/{dni}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable String dni) {
        // Primero, buscar el alumno por DNI
        Alumno alumno = alumnoRepository.findById(dni).orElse(null);

        if (alumno != null) {
            // Eliminar el alumno de la tabla Alumno
            alumnoRepository.deleteById(dni);

            // Obtener el usuario asociado al alumno
            User user = alumno.getUser();

            if (user != null) {
                // Eliminar el usuario de la tabla User
                userRepository.deleteById(user.getDni());
            }

            logger.info("Alumno con DNI {} eliminado.", dni);
            return ResponseEntity.ok(Map.of("mensaje", "Alumno eliminado con éxito"));
        } else {
            logger.warn("Intento de eliminar alumno con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/alumno/updatePassword/{dni}")
    public ResponseEntity<?> actualizarPassword(@PathVariable String dni, @RequestBody Map<String, String> payload) {
        // Obtener la nueva contraseña del payload
        String nuevaClave = payload.get("nuevaClave");

        // Obtener el alumno existente de la base de datos
        Alumno alumnoExistente = alumnoRepository.findById(dni).orElse(null);

        if (alumnoExistente != null) {
            // Obtener el usuario asociado al alumno
            User user = alumnoExistente.getUser();

            if (user != null) {
                // Actualizar la contraseña del usuario
                user.setClave(passwordEncoder.encode(nuevaClave));
                userRepository.save(user);

                return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito"));
            } else {
                logger.warn("Intento de actualizar contraseña del alumno con DNI {} que no existe.", dni);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Intento de actualizar contraseña del alumno con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }
    @Autowired
    private com.gamma.backend.service.modelservice.AlumnoService alumnoService;

    @GetMapping("/alumno/codigo_curso")
    public ResponseEntity<?> obtenerCodigoCurso(@RequestParam String dni) {
        String codigoCurso = alumnoService.obtenerCodigoGradoPorDni(dni);
        if (codigoCurso != null) {
            return ResponseEntity.ok(Map.of("codigo_curso", codigoCurso));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}