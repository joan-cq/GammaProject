package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BimestreControllerTest {

    @Mock
    private AnioEscolarService anioEscolarService;

    @Mock
    private BimestreRepository bimestreRepository;

    @InjectMocks
    private BimestreController bimestreController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarBimestresActivos() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);

        Bimestre b1 = new Bimestre();
        b1.setId(1);
        b1.setNombre("Primer Bimestre");
        b1.setAnioEscolar(anioActivo);

        Bimestre b2 = new Bimestre();
        b2.setId(2);
        b2.setNombre("Segundo Bimestre");
        b2.setAnioEscolar(anioActivo);

        List<Bimestre> bimestres = Arrays.asList(b1, b2);

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(bimestreRepository.findByAnioEscolar(anioActivo)).thenReturn(bimestres);

        // Act
        List<Bimestre> resultado = bimestreController.listarBimestresActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Primer Bimestre", resultado.get(0).getNombre());
        assertEquals("Segundo Bimestre", resultado.get(1).getNombre());
    }

    @Test
    void testListarBimestresActivos_SinAnioActivo() {
        // Arrange
        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.empty());

        // Act
        List<Bimestre> resultado = bimestreController.listarBimestresActivos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); // si retorna null puede fallar, depende de tu lógica real
    }
}
