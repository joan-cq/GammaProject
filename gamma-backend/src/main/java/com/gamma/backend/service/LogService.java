package com.gamma.backend.service;

import com.gamma.backend.model.Log;
import com.gamma.backend.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LINE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Categorías estándar que deben usar los controladores
    public static final String INFO = "INFO";
    public static final String CREACION = "CREACION";
    public static final String MODIFICACION = "MODIFICACION";
    public static final String ELIMINACION = "ELIMINACION";
    public static final String LOGIN = "LOGIN";
    public static final String ERROR = "ERROR";

    private final LogRepository logRepository;

    // Ruta donde se guardan los .log diarios. Configurable en application.properties (app.logs.path)
    @Value("${app.logs.path:./logs}")
    private String logsPath;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Registra un evento usando como autor al usuario autenticado actualmente (token JWT de la petición).
     */
    public void addLog(String categoria, String entidad, String mensaje) {
        addLog(categoria, entidad, mensaje, obtenerUsuarioActual());
    }

    /**
     * Registra un evento indicando explícitamente el DNI del autor (útil en /api/auth/login,
     * donde todavía no existe una sesión autenticada en el momento de escribir el log).
     */
    public void addLog(String categoria, String entidad, String mensaje, String usuarioDni) {
        Log log = new Log();
        log.setCategoria(categoria);
        log.setEntidad(entidad);
        log.setMensaje(mensaje);
        log.setUsuarioDni(usuarioDni != null ? usuarioDni : "SISTEMA");
        logRepository.save(log);

        escribirEnArchivo(categoria, entidad, log.getUsuarioDni(), mensaje);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    private String obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "SISTEMA";
    }

    private void escribirEnArchivo(String categoria, String entidad, String usuarioDni, String mensaje) {
        try {
            Files.createDirectories(Path.of(logsPath));
            String nombreArchivo = "gamma-" + LocalDateTime.now().format(FILE_DATE) + ".log";
            Path rutaArchivo = Path.of(logsPath, nombreArchivo);

            String linea = String.format("%s | %-13s | %-15s | usuario=%-10s | %s%n",
                    LocalDateTime.now().format(LINE_DATE),
                    categoria,
                    entidad,
                    usuarioDni,
                    mensaje);

            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo.toFile(), true))) {
                writer.print(linea);
            }
        } catch (IOException e) {
            // No queremos que un problema de disco tumbe la petición HTTP; solo lo dejamos en la consola.
            logger.error("No se pudo escribir el log en archivo ({}): {}", logsPath, e.getMessage());
        }
    }
}
