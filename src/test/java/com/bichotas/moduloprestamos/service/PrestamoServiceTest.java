package com.bichotas.moduloprestamos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public void shouldCreatePrestamoSuccess() {
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
    public void shouldCreatePrestamoLoanDateAfterReturnDate() {
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
    public void createPrestamo_InvalidState() {
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
    public void shouldGetPrestamosWithEstadoIsPrestado() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamosWithStatusPrestado = prestamoService.getPrestamosWithStatusIsPrestado();

        assertEquals(2, prestamosWithStatusPrestado.size());
        assertEquals("Prestado", prestamosWithStatusPrestado.get(0).getEstado());
        assertEquals("Prestado", prestamosWithStatusPrestado.get(1).getEstado());
    }
}