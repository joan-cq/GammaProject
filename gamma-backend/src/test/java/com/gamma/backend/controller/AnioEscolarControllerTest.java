package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.AnioEscolarRepository;
import com.gamma.backend.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnioEscolarControllerTest {

    @InjectMocks
    private AnioEscolarController controller;

    @Mock
    private AnioEscolarRepository anioEscolarRepository;

    @Mock
    private LogService logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarAniosEscolares() {
        List<AnioEscolar> mockLista = List.of(
                new AnioEscolar(1, 2024, "activo"),
                new AnioEscolar(2, 2023, "inactivo")
        );

        when(anioEscolarRepository.findAll()).thenReturn(mockLista);

        ResponseEntity<List<AnioEscolar>> response = controller.listarAniosEscolares();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(logService, times(1)).addLog(eq("INFO"), contains("listado los años escolares"));
    }

    @Test
    void testActualizarEstado() {
        Long id = 1L;
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("estado", "activo");

        AnioEscolar mockAnio = new AnioEscolar(3, 2025, "inactivo");
        when(anioEscolarRepository.findById(id)).thenReturn(Optional.of(mockAnio));
        when(anioEscolarRepository.save(any(AnioEscolar.class))).thenReturn(mockAnio);

        ResponseEntity<?> response = controller.actualizarEstado(payload);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("mensaje").toString().contains("actualizado"));
        assertEquals("activo", mockAnio.getEstado());

        verify(anioEscolarRepository).save(mockAnio);
        verify(logService).addLog(eq("INFO"), contains("actualizado el estado"));
    }

    @Test
    void testActualizarEstado_NotFound() {
        Long id = 99L;
        Map<String, Object> payload = Map.of("id", id, "estado", "activo");

        when(anioEscolarRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.actualizarEstado(payload);
        });

        assertEquals("Año escolar no encontrado con ID: " + id, exception.getMessage());
    }
}
