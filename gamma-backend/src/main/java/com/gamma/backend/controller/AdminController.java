package com.gamma.backend.controller;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/list")
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        List<Administrador> administradores = administradorRepository.findAll();
        for (Administrador administrador : administradores) {
            if (administrador.getUser() != null) {
                administrador.setRol(administrador.getUser().getRol());
                administrador.setClave(administrador.getUser().getClave());
            }
        }
        return ResponseEntity.ok(administradores);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<Administrador> actualizarAdministrador(@RequestBody Administrador administrador) {
        // Obtener el administrador existente de la base de datos
        Administrador administradorExistente = administradorRepository.findById(administrador.getDni()).orElse(null);

        if (administradorExistente != null) {
            // Actualizar los atributos del administrador existente
            administradorExistente.setNombre(administrador.getNombre());
            administradorExistente.setApellido(administrador.getApellido());
            administradorExistente.setCelular(administrador.getCelular());
            administradorExistente.setEstado(administrador.getEstado());

            // Obtener el usuario asociado al administrador
            User user = administrador.getUser();

            // Actualizar el atributo rol en el usuario si no es nulo
            if (user != null) {
                if (administradorExistente.getUser() != null) {
                    administradorExistente.getUser().setRol(user.getRol());
                    userRepository.save(administradorExistente.getUser());
                } else {
                }
            }

            Administrador administradorActualizado = administradorRepository.save(administradorExistente);
            return ResponseEntity.ok(administradorActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> agregarAdministrador(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String nombre = payload.get("nombre");
        String apellido = payload.get("apellido");
        String celular = payload.get("celular");
        String rol = payload.get("rol");
        String clave = payload.get("clave");
        String estado = payload.get("estado");

        // Crear el usuario
        User user = new User();
        user.setDni(dni);
        user.setClave(clave);
        user.setRol(rol);
        userRepository.save(user);

        // Crear el administrador
        Administrador administrador = new Administrador();
        administrador.setDni(dni);
        administrador.setNombre(nombre);
        administrador.setApellido(apellido);
        administrador.setCelular(celular);
        administrador.setEstado(estado);
        administradorRepository.save(administrador);

        return ResponseEntity.ok(Map.of("mensaje", "Administrador agregado con éxito"));
    }

    @DeleteMapping("/admin/delete/{dni}")
    public ResponseEntity<?> eliminarAdministrador(@PathVariable String dni) {
        // Primero, buscar el administrador por DNI
        Administrador administrador = administradorRepository.findById(dni).orElse(null);

        if (administrador != null) {
            // Eliminar el administrador de la tabla Administrador
            administradorRepository.deleteById(dni);

            // Obtener el usuario asociado al administrador
            User user = administrador.getUser();

            if (user != null) {
                // Eliminar el usuario de la tabla User
                userRepository.deleteById(user.getDni());
            }

            return ResponseEntity.ok(Map.of("mensaje", "Administrador eliminado con éxito"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin/updatePassword/{dni}")
    public ResponseEntity<?> actualizarPassword(@PathVariable String dni, @RequestBody Map<String, String> payload) {
        // Obtener la nueva contraseña del payload
        String nuevaClave = payload.get("nuevaClave");

        // Obtener el administrador existente de la base de datos
        Administrador administradorExistente = administradorRepository.findById(dni).orElse(null);

        if (administradorExistente != null) {
            // Obtener el usuario asociado al administrador
            User user = administradorExistente.getUser();

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


