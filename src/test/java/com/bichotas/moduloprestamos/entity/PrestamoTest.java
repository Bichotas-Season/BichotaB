package com.bichotas.moduloprestamos.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PrestamoTest {

    @Test
    void shouldSettersPrestamo(){
        Prestamo prestamo = new Prestamo();
        prestamo.setId_estudiante("5f5b3b3b1f1b3b5f5b3b3b1f");
        prestamo.setId_libro("5f5b3b3b1f1b3b5f5b3b3b1f");
        prestamo.setFecha_devolucion(LocalDate.parse("2025-09-18"));
        prestamo.setEstado("Prestado");
        prestamo.setObservaciones("El estudiante se compromete a devolver el libro en la fecha acordada");
        prestamo.setHistorial_estado("Estado de como de presta");
        prestamo.setCreado_by("5f5b3b3b1f1b3b5f5b3b3b1f");

        assertEquals("5f5b3b3b1f1b3b5f5b3b3b1f", prestamo.getId_estudiante());
        assertEquals("5f5b3b3b1f1b3b5f5b3b3b1f", prestamo.getId_libro());
        assertEquals(LocalDate.parse("2025-09-18"), prestamo.getFecha_devolucion());
        assertEquals("Prestado", prestamo.getEstado());
        assertEquals("El estudiante se compromete a devolver el libro en la fecha acordada", prestamo.getObservaciones());
        assertEquals("Estado de como de presta", prestamo.getHistorial_estado());
        assertEquals("5f5b3b3b1f1b3b5f5b3b3b1f", prestamo.getCreado_by());
    }

    @Test
    void shouldBuilderPrestamo(){
        Prestamo prestamo = Prestamo.builder()
            .id_estudiante("5f5b3b3b1f1b3b5f5b3b3b1f")
            .id_libro("5f5b3b3b1f1b3b5f5b3b3b1f")
            .fecha_devolucion(LocalDate.parse("2025-09-18"))
            .estado("Prestado")
            .observaciones("El estudiante se compromete a devolver el libro en la fecha acordada")
            .historial_estado("Estado de como de presta")
            .creado_by("5f5b3b3b1f1b3b5f5b3b3b1f")
            .build();

        assertEquals("5f5b3b3b1f1b3b5f5b3b3b1f", prestamo.getId_estudiante());
    }
}