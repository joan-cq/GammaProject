package com.gamma.backend.controller;

import java.util.List;
import java.util.Map;

import com.gamma.backend.model.Alumno;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AlumnoRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlumnoController {
    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnioEscolarService anioEscolarService;

    @GetMapping("/alumno/list")
    public ResponseEntity<List<Alumno>> listarAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        for (Alumno alumno : alumnos) {
            if (alumno.getUser() != null) {
                alumno.setRol(alumno.getUser().getRol());
                alumno.setClave(alumno.getUser().getClave());
            }
        }
        logger.info("Listado de alumnos solicitado.");
        return ResponseEntity.ok(alumnos);
    }

    @PutMapping("/alumno/update")
    public ResponseEntity<Alumno> actualizarAlumno(@RequestBody Alumno Alumno) {
        // Obtener el Alumno existente de la base de datos
        Alumno AlumnoExistente = alumnoRepository.findById(Alumno.getDni()).orElse(null);

        if (AlumnoExistente != null) {
            // Actualizar los atributos del Alumno existente
            AlumnoExistente.setNombre(Alumno.getNombre());
            AlumnoExistente.setApellido(Alumno.getApellido());
            AlumnoExistente.setCelularApoderado(Alumno.getCelularApoderado());
            AlumnoExistente.setGenero(Alumno.getGenero());
            AlumnoExistente.setEstado(Alumno.getEstado());

            // Obtener el usuario asociado al Alumno
            User user = Alumno.getUser();

            // Actualizar el atributo rol en el usuario si no es nulo
            if (user != null) {
                if (AlumnoExistente.getUser() != null) {
                    AlumnoExistente.getUser().setRol(user.getRol());
                    userRepository.save(AlumnoExistente.getUser());
                } else {
                }
            }

            Alumno AlumnoActualizado = alumnoRepository.save(AlumnoExistente);
            logger.info("Alumno con DNI {} actualizado.", Alumno.getDni());
            return ResponseEntity.ok(AlumnoActualizado);
        } else {
            logger.warn("Intento de actualizar alumno con DNI {} que no existe.", Alumno.getDni());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/alumno/add")
    public ResponseEntity<?> agregarAlumno(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String nombre = payload.get("nombre");
        String apellido = payload.get("apellido");
        String celularApoderado = payload.get("celularApoderado");
        String rol = payload.get("rol");
        String clave = payload.get("clave");
        String estado = payload.get("estado");
        String genero = payload.get("genero");

        // Crear el usuario
        User user = new User();
        user.setDni(dni);
        user.setClave(clave);
        user.setRol(rol);
        userRepository.save(user);

        // Crear el Alumno
        Alumno Alumno = new Alumno();
        Alumno.setDni(dni);
        Alumno.setNombre(nombre);
        Alumno.setApellido(apellido);
        Alumno.setCelularApoderado(celularApoderado);
        Alumno.setEstado(estado);
        Alumno.setGenero(genero);
        alumnoRepository.save(Alumno);
        logger.info("Alumno con DNI {} agregado.", dni);

        return ResponseEntity.ok(Map.of("mensaje", "Alumno agregado con éxito"));
    }

    @DeleteMapping("/alumno/delete/{dni}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable String dni) {
        // Primero, buscar el Alumno por DNI
        Alumno Alumno = alumnoRepository.findById(dni).orElse(null);

        if (Alumno != null) {
            // Eliminar el Alumno de la tabla Alumno
            alumnoRepository.deleteById(dni);

            // Obtener el usuario asociado al Alumno
            User user = Alumno.getUser();

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

        // Obtener el Alumno existente de la base de datos
        Alumno AlumnoExistente = alumnoRepository.findById(dni).orElse(null);

        if (AlumnoExistente != null) {
            // Obtener el usuario asociado al Alumno
            User user = AlumnoExistente.getUser();

            if (user != null) {
                // Actualizar la contraseña del usuario
                user.setClave(nuevaClave);
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
}