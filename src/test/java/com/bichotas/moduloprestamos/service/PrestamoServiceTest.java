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

    @Test
    public void shouldThrowExceptionWhenPrestamoNotFound() {
        when(prestamoRepository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.deletePrestamoById("123");
        });
    }

    @Test
    public void shouldGetPrestamos(){
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("1233");
        prestamo2.setIdLibro("4564");
        prestamo2.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> prestamos = prestamoService.getPrestamos();

        assertEquals(2, prestamos.size());
        assertEquals("Prestado", prestamos.get(0).getEstado());
        assertEquals("Prestado", prestamos.get(1).getEstado());
    }

    @Test
    public void shouldGetPrestamoByIdSuccess() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(new org.bson.types.ObjectId());
        prestamo.setIdEstudiante("123");
        prestamo.setIdLibro("456");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        Prestamo result = prestamoService.getPrestamoById(prestamo.getId().toString());

        assertNotNull(result);
        assertEquals("Prestado", result.getEstado());
    }

    @Test
    public void shouldThrowExceptionWhenPrestamoIdNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionPrestamoIdNotFound.class, () -> {
            prestamoService.getPrestamoById("123");
        });
    }

    @Test
    public void shouldGetPrestamosByIsbnSuccess() {
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
    public void shouldThrowExceptionWhenIsbnNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionBookIsAvailable.class, () -> {
            prestamoService.getPrestamosByIsbn("isbn123");
        });
    }

    @Test
    public void shouldGetPrestamosByIdEstudianteSuccess() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setIdEstudiante("123");
        prestamo1.setIdLibro("456");
        prestamo1.setEstado("Prestado");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setIdEstudiante("123");
        prestamo2.setIdLibro("789");
        prestamo2.setEstado("Devuelto");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> result = prestamoService.getPrestamosByIdEstudiante("123");

        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getIdEstudiante());
        assertEquals("123", result.get(1).getIdEstudiante());
    }

    @Test
    public void shouldThrowExceptionWhenEstudianteIdNotFound() {
        when(prestamoRepository.findAll()).thenReturn(List.of());

        assertThrows(PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo.class, () -> {
            prestamoService.getPrestamosByIdEstudiante("123");
        });
    }

    @Test
    public void shouldReturnEmptyListWhenNoPrestamosWithGivenEstudianteId() {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdEstudiante("456");
        prestamo.setIdLibro("789");
        prestamo.setEstado("Prestado");

        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        assertThrows(PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo.class, () -> {
            prestamoService.getPrestamosByIdEstudiante("123");
        });
    }
}