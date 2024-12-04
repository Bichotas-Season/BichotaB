package com.bichotas.moduloprestamos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.repository.PrestamoRepository;

class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePrestamoSuccess() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamo);
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        Prestamo result = prestamoService.createPrestamo(prestamo);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getFechaPrestamo());
        assertEquals(LocalDateTime.now().getDayOfYear(), result.getFechaCreacion().getDayOfYear());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldCreatePrestamoLoanDateAfterReturnDate() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");
        prestamo.setFechaDevolucion(LocalDate.now().minusDays(1));

        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionTimeError.class, () -> {
            prestamoService.createPrestamo(prestamo);
        });
    }

    @Test
    void createPrestamo_InvalidState() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("InvalidState");

        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionStateError.class, () -> {
            prestamoService.createPrestamo(prestamo);
        });
    }

    @Test
    void shouldGetPrestamosWithEstadoIsPrestado() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Prestado");

        when(prestamoRepository.findByEstado("Prestado")).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamosWithStatusPrestado = prestamoService.getPrestamos("Prestado");

        assertEquals(2, prestamosWithStatusPrestado.size());
        assertEquals("Prestado", prestamosWithStatusPrestado.get(0).getEstado());
        assertEquals("Prestado", prestamosWithStatusPrestado.get(1).getEstado());
    }

    @Test
    void shouldGetPrestamosWithEstadoIsVencido() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Vencido");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Vencido");

        when(prestamoRepository.findByEstado("Vencido")).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamosWithStatusVencido = prestamoService.getPrestamos("Vencido");

        assertEquals(2, prestamosWithStatusVencido.size());
        assertEquals("Vencido", prestamosWithStatusVencido.get(0).getEstado());
        assertEquals("Vencido", prestamosWithStatusVencido.get(1).getEstado());
    }

    /*
    @Test
    void shouldGetPrestamosWithEstadoIsDevuelto() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Devuelto");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findByEstado("Devuelto")).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamosWithStatusDevuelto = prestamoService.getPrestamos("Devuelto");

        assertEquals(2, prestamosWithStatusDevuelto.size());
        assertEquals("Devuelto", prestamosWithStatusDevuelto.get(0).getEstado());
        assertEquals("Devuelto", prestamosWithStatusDevuelto.get(1).getEstado());
    }*/

    @Test
    void shouldGetAllPrestamosWhenEstadoIsNull() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamos = prestamoService.getPrestamos(null);

        assertEquals(2, prestamos.size());
        assertEquals("Prestado", prestamos.get(0).getEstado());
        assertEquals("Devuelto", prestamos.get(1).getEstado());
    }

    @Test
    void shouldThrowExceptionForInvalidEstado() {
        assertThrows(PrestamosException.PrestamosExceptionStateError.class, () -> {
            prestamoService.getPrestamos("InvalidEstado");
        });
    }

    @Test
    void shouldThrowExceptionWhenPrestamoNotFound() {
        when(prestamoRepository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.deletePrestamoById("123");
        });
    }

    @Test
    public void shouldGetPrestamos() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamos = prestamoService.getPrestamos(null);

        assertEquals(2, prestamos.size());
        assertEquals("Prestado", prestamos.get(0).getEstado());
        assertEquals("Prestado", prestamos.get(1).getEstado());
    }

    @Test
    void shouldGetPrestamoByIdWhenExists() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId("678");
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById("678")).thenReturn(java.util.Optional.of(prestamo));

        Prestamo result = prestamoService.getPrestamoById("678");

        assertNotNull(result);
        assertEquals("678", result.getId());
        assertEquals("123", result.getIdEstudiante());
        assertEquals("456", result.getIdLibro());
        assertEquals("Prestado", result.getEstado());
    }

    @Test
    void shouldThrowExceptionWhenPrestamoIdDoesNotExist() {
        when(prestamoRepository.findById("678")).thenReturn(java.util.Optional.empty());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.getPrestamoById("678");
        });
    }

    @Test
    void shouldThrowExceptionWhenPrestamoIdNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.getPrestamoById("123");
        });
    }

    @Test
    void shouldGetPrestamosByIsbnSuccess() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("isbn123");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("456");
        prestamo2.setIdLibro("isbn123");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> result = prestamoService.getPrestamosByIsbn("isbn123");

        assertEquals(2, result.size());
        assertEquals("isbn123", result.get(0).getIdLibro());
        assertEquals("isbn123", result.get(1).getIdLibro());
    }

    @Test
    void shouldThrowExceptionWhenIsbnNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionBookIsAvailable.class, () -> {
            prestamoService.getPrestamosByIsbn("isbn123");
        });
    }

    @Test
    void shouldGetPrestamosByIdEstudianteWhenExists() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("123");
        prestamo2.setIdLibro("789");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findByIdEstudiante("123")).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> result = prestamoService.getPrestamosByIdEstudiante("123");

        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getIdEstudiante());
        assertEquals("123", result.get(1).getIdEstudiante());
    }

    @Test
    void shouldThrowExceptionWhenEstudianteIdNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo.class, () -> {
            prestamoService.getPrestamosByIdEstudiante("123");
        });
    }

    @Test
    void shouldReturnEmptyListWhenNoPrestamosWithGivenEstudianteId() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("456");
        prestamo.setIdLibro("789");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        assertThrows(PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo.class, () -> {
            prestamoService.getPrestamosByIdEstudiante("123");
        });
    }


    @Test
    void shouldDeletePrestamoWhenNotReturnedOrOverdue() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        Prestamo result = prestamoService.deletePrestamoById("123");

        assertNotNull(result);
        verify(prestamoRepository, times(1)).deleteById(prestamo.getId());
    }

    @Test
    void shouldThrowExceptionWhenPrestamoIsReturned() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Devuelto");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        assertThrows(PrestamosException.PrestamosExceptionStateError.class, () -> {
            prestamoService.deletePrestamoById("123");
        });
    }

    @Test
    void shouldThrowExceptionWhenPrestamoIsOverdue() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Vencido");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        assertThrows(PrestamosException.PrestamosExceptionStateError.class, () -> {
            prestamoService.deletePrestamoById("123");
        });
    }

    @Test
    void shouldUpdateObservacionesSuccessfully() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        prestamoService.updatePrestamo("123", Map.of("observaciones", "New observation"));

        assertEquals("New observation", prestamo.getObservaciones());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldUpdateEstadoSuccessfully() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        prestamoService.updatePrestamo("123", Map.of("estado", "Devuelto"));

        assertEquals("Devuelto", prestamo.getEstado());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldUpdateFechaDevolucionSuccessfullyWithString() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        prestamoService.updatePrestamo("123", Map.of("fecha_devolucion", "2023-10-10T10:10:10"));

        assertEquals(LocalDate.of(2023, 10, 10), prestamo.getFechaDevolucion());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldUpdateFechaDevolucionSuccessfullyWithLocalDateTime() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        prestamoService.updatePrestamo("123", Map.of("fecha_devolucion", LocalDateTime.of(2023, 10, 10, 10, 10, 10)));

        assertEquals(LocalDate.of(2023, 10, 10), prestamo.getFechaDevolucion());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldThrowExceptionForInvalidFechaDevolucionFormat() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.updatePrestamo("123", Map.of("fecha_devolucion", 12345));
        });
    }

    @Test
    void shouldUpdateHistorialEstadoSuccessfully() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        prestamoService.updatePrestamo("123", Map.of("historial_estado", "New history"));

        assertEquals("New history", prestamo.getHistorialEstado());
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void shouldThrowExceptionForInvalidAttribute() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(prestamo));

        assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.updatePrestamo("123", Map.of("invalid_attribute", "value"));
        });
    }

    @Test
    void shouldGellAllPrestamosWithEstadoIsDevuelto(){
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Devuelto");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findByEstado("Devuelto")).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamosWithStatusDevuelto = prestamoService.getPrestamos("Devuelto");

        assertEquals(2, prestamosWithStatusDevuelto.size());
        assertEquals("Devuelto", prestamosWithStatusDevuelto.get(0).getEstado());
        assertEquals("Devuelto", prestamosWithStatusDevuelto.get(1).getEstado());
    }

    @Test
    void shouldThrowExceptionWhenPrestamoNotFound2() {
        when(prestamoRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.devolverPrestamo("1", "Entregado");
        });
    }

    @Test
    void testDevolverPrestamo_Success() {
        String prestamoId = "123";
        String estado = "En buen estado";

        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoId);
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

        Prestamo returnedPrestamo = prestamoService.devolverPrestamo(prestamoId, estado);

        assertNotNull(returnedPrestamo);
        assertEquals("Devuelto", returnedPrestamo.getEstado());
        assertEquals(estado, returnedPrestamo.getHistorialEstado());
        assertEquals(LocalDate.now(), returnedPrestamo.getFechaDevolucion());

        verify(prestamoRepository).findById(prestamoId);
    }

    @Test
    void testDevolverPrestamo_PrestamoNotFound() {
        String prestamoId = "999";
        String estado = "En buen estado";

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.empty());

        assertThrows(PrestamosException.class, () -> {
            prestamoService.devolverPrestamo(prestamoId, estado);
        });

        verify(prestamoRepository).findById(prestamoId);
    }

    @Test
    void testDevolverPrestamo_DifferentEstadoValues() {
        String prestamoId = "456";
        String[] estadoVariants = {
                "Ligeramente deteriorado",
                "Muy deteriorado",
                "Perfecto estado"
        };

        for (String estado : estadoVariants) {
            Prestamo prestamo = new Prestamo();
            prestamo.setId(prestamoId);
            prestamo.setEstado("Prestado");

            when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

            Prestamo returnedPrestamo = prestamoService.devolverPrestamo(prestamoId, estado);

            assertEquals("Devuelto", returnedPrestamo.getEstado());
            assertEquals(estado, returnedPrestamo.getHistorialEstado());
            assertEquals(LocalDate.now(), returnedPrestamo.getFechaDevolucion());

            reset(prestamoRepository);
        }
    }

}

