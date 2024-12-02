package com.bichotas.moduloprestamos.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoTest {


    @Test
    void shouldCreatePrestamoSuccessfully() {
        Prestamo prestamo = Prestamo.builder()
                .idEstudiante("123")
                .idLibro("456")
                .fechaPrestamo(LocalDate.now())
                .estado("Prestado")
                .creadoBy("admin")
                .build();

        assertEquals("123", prestamo.getIdEstudiante());
        assertEquals("456", prestamo.getIdLibro());
        assertEquals("Prestado", prestamo.getEstado());
        assertEquals("admin", prestamo.getCreadoBy());
    }

}