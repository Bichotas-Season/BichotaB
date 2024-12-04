package com.bichotas.moduloprestamos.controller;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertEquals(Collections.singletonMap("prestamo", prestamo), response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPrestamoNotFound() {
        when(prestamoService.getPrestamoById("999")).thenReturn(null);

        ResponseEntity<?> response = prestamoController.getPrestamoById("999");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("prestamo", null), response.getBody());
    }

    @Test
    void shouldReturnErrorWhenGetAllPrestamos() {
        when(prestamoService.getPrestamos("das"))
                .thenThrow(new PrestamosException.PrestamosExceptionStateError("El estado solo puede ser Prestado, Vencido o Devuelto"));
        ResponseEntity<?> response = prestamoController.getPrestamos("das");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "El estado solo puede ser Prestado, Vencido o Devuelto"), response.getBody());
    }

    @Test
    void shouldDeletePrestamoSuccessfully() {
        String prestamoId = "123";
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoId);
        prestamo.setEstado("Prestado");

        when(prestamoService.getPrestamoById(prestamoId)).thenReturn(prestamo);

        ResponseEntity<?> response = prestamoController.deletePrestamo(prestamoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("message", "Prestamo eliminado correctamente"), response.getBody());
        verify(prestamoService).deletePrestamoById(prestamoId);
    }

    @Test
    void shouldReturnErrorWhenPrestamoIsReturned() {
        String prestamoId = "123";
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoId);
        prestamo.setEstado("Devuelto");

        when(prestamoService.getPrestamoById(prestamoId)).thenReturn(prestamo);
        doThrow(new PrestamosException.PrestamosExceptionStateError("El préstamo ya ha sido devuelto"))
                .when(prestamoService).deletePrestamoById(prestamoId);

        ResponseEntity<?> response = prestamoController.deletePrestamo(prestamoId);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "El préstamo ya ha sido devuelto"), response.getBody());
    }

    @Test
    void shouldReturnErrorWhenPrestamoIsOverdue() {
        String prestamoId = "123";
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoId);
        prestamo.setEstado("Vencido");

        when(prestamoService.getPrestamoById(prestamoId)).thenReturn(prestamo);
        doThrow(new PrestamosException.PrestamosExceptionStateError("El préstamo está vencido"))
                .when(prestamoService).deletePrestamoById(prestamoId);

        ResponseEntity<?> response = prestamoController.deletePrestamo(prestamoId);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "El préstamo está vencido"), response.getBody());
    }

    @Test
    void shouldReturnBadRequestForInvalidArguments() {
        String prestamoId = "123";
        String estado = "InvalidEstado";

        doThrow(new IllegalArgumentException("Estado inválido"))
                .when(prestamoService).devolverPrestamo(prestamoId, estado);

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Estado inválido"), response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPrestamoDoesNotExist() {
        String prestamoId = "999";
        String estado = "Entregado";

        doThrow(new NoSuchElementException("Prestamo no encontrado"))
                .when(prestamoService).devolverPrestamo(prestamoId, estado);

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Prestamo no encontrado"), response.getBody());
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedExceptions() {
        String prestamoId = "123";
        String estado = "Entregado";

        doThrow(new RuntimeException("Error inesperado"))
                .when(prestamoService).devolverPrestamo(prestamoId, estado);

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(Collections.singletonMap("error", "Error inesperado"), response.getBody());
    }

    @Test
    void shouldReturnPrestamoSuccessfully() {
        String prestamoId = "1";
        String estado = "Entregado";
        Prestamo mockPrestamo = new Prestamo();
        when(prestamoService.devolverPrestamo(prestamoId, estado)).thenReturn(mockPrestamo);

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonMap("message", "Prestamo devuelto correctamente"), response.getBody());
        verify(prestamoService).devolverPrestamo(prestamoId, estado);
    }

    @Test
    void shouldReturnPrestamoSuccessfully1() {
        String prestamoId = "1";
        String estado = "Entregado";

        Prestamo mockPrestamo = new Prestamo();
        mockPrestamo.setId(prestamoId);
        mockPrestamo.setEstado("Pendiente");
        mockPrestamo.setFechaPrestamo(LocalDate.now().minusDays(7));

        when(prestamoService.devolverPrestamo(prestamoId, estado)).thenAnswer(invocation -> {
            mockPrestamo.setEstado("Devuelto");
            mockPrestamo.setFechaDevolucion(LocalDate.now());
            return mockPrestamo;
        });

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonMap("message", "Prestamo devuelto correctamente"), response.getBody());

        assertEquals("Devuelto", mockPrestamo.getEstado());
        assertNotNull(mockPrestamo.getFechaDevolucion());

        verify(prestamoService).devolverPrestamo(prestamoId, estado);
    }


    @Test
    void shouldHandleIllegalArgumentException() {
        String prestamoId = "1";
        String estado = "Entregado";
        when(prestamoService.devolverPrestamo(prestamoId, estado))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.singletonMap("error", "Invalid argument"), response.getBody());
    }

    @Test
    void shouldHandleNoSuchElementException() {
        String prestamoId = "1";
        String estado = "Entregado";
        when(prestamoService.devolverPrestamo(prestamoId, estado))
                .thenThrow(new NoSuchElementException("Prestamo not found"));

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.singletonMap("error", "Prestamo not found"), response.getBody());
    }

    @Test
    void shouldHandleGeneralException() {
        String prestamoId = "1";
        String estado = "Entregado";
        when(prestamoService.devolverPrestamo(prestamoId, estado))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = prestamoController.devolverPrestamo(prestamoId, estado);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Collections.singletonMap("error", "Unexpected error"), response.getBody());
    }

}
