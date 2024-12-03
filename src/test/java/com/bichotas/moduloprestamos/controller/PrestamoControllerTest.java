package com.bichotas.moduloprestamos.controller;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PrestamoControllerTest {

    @Mock
    private PrestamoService prestamoService;

    @InjectMocks
    private PrestamoController prestamoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllPrestamos() {
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


    @Test
    void shouldReturnPrestamoById() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId("1");
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoService.getPrestamoById("1")).thenReturn(prestamo);

        ResponseEntity<?> response = prestamoController.getPrestamoById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamo", prestamo), response.getBody());
    }


    @Test
    void shouldReturnPrestamosByIsbn() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("789");
        prestamo2.setIdLibro("456");
        prestamo2.setEstado("Devuelto");

        List<Prestamo> prestamos = List.of(prestamo1, prestamo2);

        when(prestamoService.getPrestamosByIsbn("456")).thenReturn(prestamos);

        ResponseEntity<?> response = prestamoController.getPrestamosByIsbn("456");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamos", prestamos), response.getBody());
    }

    @Test
    void shouldReturnPrestamosByEstudianteId() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("123");
        prestamo2.setIdLibro("789");
        prestamo2.setEstado("Devuelto");

        List<Prestamo> prestamos = List.of(prestamo1, prestamo2);

        when(prestamoService.getPrestamosByIdEstudiante("123")).thenReturn(prestamos);

        ResponseEntity<?> response = prestamoController.getPrestamosByEstudiante("123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamos", prestamos), response.getBody());
    }


    @Test
    void shouldUpdatePrestamoById() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doNothing().when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("message", "Prestamo actualizado correctamente"), response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPrestamo() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doThrow(new NoSuchElementException("Prestamo not found")).when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Prestamo not found"), response.getBody());
    }

    @Test
    void shouldUpdatePrestamoSuccessfully() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doNothing().when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("message", "Prestamo actualizado correctamente"), response.getBody());
    }

    @Test
    void shouldReturnBadRequestForInvalidUpdate() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doThrow(new IllegalArgumentException("Invalid update")).when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Invalid update"), response.getBody());
    }

    @Test
    void shouldReturnNotFoundForNonExistentPrestamo() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doThrow(new NoSuchElementException("Prestamo not found")).when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Prestamo not found"), response.getBody());
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedException() {
        Map<String, Object> updates = Map.of("estado", "Devuelto");

        doThrow(new RuntimeException("Unexpected error")).when(prestamoService).updatePrestamo("1", updates);

        ResponseEntity<?> response = prestamoController.updatePrestamo("1", updates);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Unexpected error"), response.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoPrestamos() {
        when(prestamoService.getPrestamos(null)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = prestamoController.getPrestamos(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamos", Collections.emptyList()), response.getBody());
    }

    @Test
    void shouldAddPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoService.createPrestamo(prestamo)).thenReturn(prestamo);

        ResponseEntity<?> response = prestamoController.createPrestamo(prestamo);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamo",prestamo), response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPrestamoNotFound() {
        when(prestamoService.getPrestamoById("999")).thenReturn(null);

        ResponseEntity<?> response = prestamoController.getPrestamoById("999");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamo", null), response.getBody());
    }
}
