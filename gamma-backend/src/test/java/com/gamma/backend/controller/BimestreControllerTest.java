package com.gamma.backend.controller;

import com.gamma.backend.model.AnioEscolar;
import com.gamma.backend.model.Bimestre;
import com.gamma.backend.repository.BimestreRepository;
import com.gamma.backend.service.modelservice.AnioEscolarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    void listarBimestresActivos_ReturnsListOfBimestres() {
        // Arrange
        AnioEscolar anioActivo = new AnioEscolar();
        anioActivo.setId(1);
        anioActivo.setAnio(2024);
        anioActivo.setEstado("ACTIVO");

        Bimestre bimestre1 = new Bimestre();
        bimestre1.setId(1);
        bimestre1.setNombre("Bimestre 1");
        bimestre1.setAnioEscolar(anioActivo);

        Bimestre bimestre2 = new Bimestre();
        bimestre2.setId(2);
        bimestre2.setNombre("Bimestre 2");
        bimestre2.setAnioEscolar(anioActivo);

        when(anioEscolarService.obtenerAnioActivo()).thenReturn(Optional.of(anioActivo));
        when(bimestreRepository.findByAnioEscolar(anioActivo)).thenReturn(List.of(bimestre1, bimestre2));

        // Act
        List<Bimestre> response = bimestreController.listarBimestresActivos();

        // Assert
        assertEquals(2, response.size());
    }
}