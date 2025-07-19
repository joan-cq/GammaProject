package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.repository.AnioEscolarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AnioEscolarControllerTest {

    @Mock
    private AnioEscolarRepository anioEscolarRepository;

    @InjectMocks
    private AnioEscolarController anioEscolarController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarAniosEscolares_ReturnsOk() {
        // Arrange
        AnioEscolar anio1 = new AnioEscolar();
        anio1.setId(1);
        anio1.setAnio(2023);
        anio1.setEstado("CERRADO");

        AnioEscolar anio2 = new AnioEscolar();
        anio2.setId(2);
        anio2.setAnio(2024);
        anio2.setEstado("ACTIVO");

        when(anioEscolarRepository.findAll()).thenReturn(List.of(anio1, anio2));

        // Act
        ResponseEntity<List<AnioEscolar>> response = anioEscolarController.listarAniosEscolares();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void actualizarEstado_ReturnsOk() {
        // Arrange
        AnioEscolar anio = new AnioEscolar();
        anio.setId(1);
        anio.setAnio(2023);
        anio.setEstado("CERRADO");

        Map<String, Object> payload = Map.of("id", 1, "estado", "ACTIVO");

        when(anioEscolarRepository.findById(1L)).thenReturn(Optional.of(anio));
        when(anioEscolarRepository.save(any(AnioEscolar.class))).thenReturn(anio);

        // Act
        ResponseEntity<?> response = anioEscolarController.actualizarEstado(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("mensaje", "Año escolar actualizado con éxito"), response.getBody());
        verify(anioEscolarRepository, times(1)).save(anio);
        assertEquals("ACTIVO", anio.getEstado());
    }
}