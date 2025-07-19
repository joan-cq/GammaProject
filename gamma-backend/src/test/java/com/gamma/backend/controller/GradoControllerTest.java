package com.gamma.backend.controller;

import com.gamma.backend.model.Grado;
import com.gamma.backend.repository.GradoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GradoControllerTest {

    @Mock
    private GradoRepository gradoRepository;

    @InjectMocks
    private GradoController gradoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarGrados_ReturnsListOfGrados() {
        // Arrange
        Grado grado1 = new Grado();
        grado1.setCodigoGrado("G001");
        grado1.setNombreGrado("Primer Grado");

        Grado grado2 = new Grado();
        grado2.setCodigoGrado("G002");
        grado2.setNombreGrado("Segundo Grado");

        when(gradoRepository.findAll()).thenReturn(List.of(grado1, grado2));

        // Act
        List<Grado> response = gradoController.listarGrados();

        // Assert
        assertEquals(2, response.size());
    }

    @Test
    void agregarGrado_ReturnsSavedGrado() {
        // Arrange
        Grado grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        when(gradoRepository.save(any(Grado.class))).thenReturn(grado);

        // Act
        Grado response = gradoController.agregarGrado(grado);

        // Assert
        assertEquals("G001", response.getCodigoGrado());
        verify(gradoRepository, times(1)).save(grado);
    }

    @Test
    void actualizarGrado_ReturnsUpdatedGrado() {
        // Arrange
        Grado grado = new Grado();
        grado.setCodigoGrado("G001");
        grado.setNombreGrado("Primer Grado");

        when(gradoRepository.save(any(Grado.class))).thenReturn(grado);

        // Act
        Grado response = gradoController.actualizarGrado(grado);

        // Assert
        assertEquals("G001", response.getCodigoGrado());
        verify(gradoRepository, times(1)).save(grado);
    }

    @Test
    void eliminarGrado_CallsDeleteById() {
        // Arrange
        String codigoGrado = "G001";

        // Act
        gradoController.eliminarGrado(codigoGrado);

        // Assert
        verify(gradoRepository, times(1)).deleteById(codigoGrado);
    }
}