package com.gamma.backend.controller;

import com.gamma.backend.model.Profesor;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.ProfesorRepository;
import com.gamma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.gamma.backend.model.Curso;
import com.gamma.backend.repository.CursoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProfesorController {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profesor/list")
    public ResponseEntity<List<Profesor>> listarProfesores() {
        List<Profesor> profesores = profesorRepository.findAll();
        for (Profesor profesor : profesores) {
            if (profesor.getUser() != null && profesor.getCurso() != null) {
                profesor.setCodigoCurso(profesor.getCurso().getCodigoCurso());
                profesor.setRol(profesor.getUser().getRol());
                profesor.setClave(profesor.getUser().getClave());
            }
        }
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
            return ResponseEntity.ok(profesorActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profesor/add")
    public ResponseEntity<?> agregarProfesor(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String nombre = payload.get("nombre");
        String apellido = payload.get("apellido");
        String celular = payload.get("celular");
        String cursoId = payload.get("curso");
        String rol = payload.get("rol");
        String clave = payload.get("clave");
        String estado = payload.get("estado");

        // Crear el usuario
        User user = new User();
        user.setDni(dni);
        user.setClave(clave);
        user.setRol(rol);
        userRepository.save(user);

        // Crear el profesor
        Profesor profesor = new Profesor();
        profesor.setDni(dni);
        profesor.setNombre(nombre);
        profesor.setApellido(apellido);
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        profesor.setCurso(curso);
        profesor.setCelular(celular);
        profesor.setEstado(estado);
        profesorRepository.save(profesor);

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

            return ResponseEntity.ok(Map.of("mensaje", "Profesor eliminado con éxito"));
        } else {
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
                user.setClave(nuevaClave);
                userRepository.save(user);

                return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}