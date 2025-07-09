package com.gamma.backend.controller;

import com.gamma.backend.model.Administrador;
import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.User;
import com.gamma.backend.repository.AdministradorRepository;
import com.gamma.backend.repository.UserRepository;
import com.gamma.backend.service.otherservice.AnioEscolarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnioEscolarService anioEscolarService;

    @GetMapping("/admin/list")
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        List<AnioEscolar> aniosActivos = anioEscolarService.obtenerAniosActivos();

        if (aniosActivos.isEmpty()) {
            logger.warn("No hay años escolares activos al listar administradores.");
            return ResponseEntity.ok(List.of()); // Lista vacía
        }

        List<Administrador> administradores = administradorRepository.findByAnioEscolarIn(aniosActivos);

        for (Administrador administrador : administradores) {
            if (administrador.getUser() != null) {
                administrador.setRol(administrador.getUser().getRol());
                administrador.setClave(administrador.getUser().getClave());
            }
        }

        logger.info("Listado de administradores solicitado.");
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
            logger.info("Administrador con DNI {} actualizado.", administrador.getDni());
            return ResponseEntity.ok(administradorActualizado);
        } else {
            logger.warn("Intento de actualizar administrador con DNI {} que no existe.", administrador.getDni());
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

        AnioEscolar anioEscolar = anioEscolarService.obtenerAnioActivo()
        .orElseThrow(() -> new RuntimeException("No hay año escolar activo"));

        administrador.setAnioEscolar(anioEscolar);
        administrador.setEstado(estado);
        administradorRepository.save(administrador);
        logger.info("Administrador con DNI {} agregado.", dni);

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

            logger.info("Administrador con DNI {} eliminado.", dni);
            return ResponseEntity.ok(Map.of("mensaje", "Administrador eliminado con éxito"));
        } else {
            logger.warn("Intento de eliminar administrador con DNI {} que no existe.", dni);
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
                logger.info("Contraseña del administrador con DNI {} actualizada.", dni);

                return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada con éxito"));
            } else {
                logger.warn("Intento de actualizar contraseña del administrador con DNI {} que no existe.", dni);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Intento de actualizar contraseña del administrador con DNI {} que no existe.", dni);
            return ResponseEntity.notFound().build();
        }
    }
}


