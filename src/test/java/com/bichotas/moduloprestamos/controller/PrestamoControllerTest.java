package com.bichotas.moduloprestamos.controller;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrestamoControllerTest {

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private PrestamoController prestamoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllPrestamos() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");
        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("789");
        prestamo2.setIdLibro("101");
        prestamo2.setEstado("Prestado");

        List<Prestamo> prestamos = new ArrayList<>();
        prestamos.add(prestamo1);
        prestamos.add(prestamo2);

        when(prestamoService.getPrestamos(null)).thenReturn(prestamos);

        ResponseEntity<?> response = prestamoController.getPrestamos(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamos", prestamos), response.getBody());
    }
}
