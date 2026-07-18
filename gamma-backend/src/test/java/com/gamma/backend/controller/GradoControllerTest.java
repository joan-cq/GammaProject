package com.gamma.backend.controller;

import com.gamma.backend.model.Grado;
import com.gamma.backend.repository.GradoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    // Test GET /grado/list
    @Test
    void testListarGrados() {
        Grado g1 = new Grado();
        g1.setCodigoGrado("G1");
        g1.setNombreGrado("Primero");

        Grado g2 = new Grado();
        g2.setCodigoGrado("G2");
        g2.setNombreGrado("Segundo");

        List<Grado> lista = Arrays.asList(g1, g2);

        when(gradoRepository.findAll()).thenReturn(lista);

        List<Grado> resultado = gradoController.listarGrados();

        assertEquals(2, resultado.size());
        assertEquals("Primero", resultado.get(0).getNombreGrado());
        verify(gradoRepository).findAll();
    }

    // Test POST /grado/add
    @Test
    void testAgregarGrado() {
        Grado grado = new Grado();
        grado.setCodigoGrado("G3");
        grado.setNombreGrado("Tercero");

        when(gradoRepository.save(grado)).thenReturn(grado);

        Grado resultado = gradoController.agregarGrado(grado);

        assertNotNull(resultado);
        assertEquals("Tercero", resultado.getNombreGrado());
        verify(gradoRepository).save(grado);
    }

    // Test PUT /grado/update
    @Test
    void testActualizarGrado() {
        Grado grado = new Grado();
        grado.setCodigoGrado("G4");
        grado.setNombreGrado("Cuarto");

        when(gradoRepository.save(grado)).thenReturn(grado);

        Grado resultado = gradoController.actualizarGrado(grado);

        assertEquals("Cuarto", resultado.getNombreGrado());
        verify(gradoRepository).save(grado);
    }

    // Test DELETE /grado/delete/{codigoGrado}
    @Test
    void testEliminarGrado() {
        String codigo = "G5";

        doNothing().when(gradoRepository).deleteById(codigo);

        gradoController.eliminarGrado(codigo);

        verify(gradoRepository).deleteById(codigo);
    }
}
