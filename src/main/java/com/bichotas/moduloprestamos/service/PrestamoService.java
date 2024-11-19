package com.bichotas.moduloprestamos.service;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.repository.PrestamoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public Prestamo createPrestamo(Prestamo prestamo) {
        prestamo.setFecha_prestamo(LocalDate.now());
        prestamo.setFecha_creacion(LocalDateTime.now());
        createPrestamoValidations(prestamo);
        return prestamoRepository.save(prestamo);
    }

    private void createPrestamoValidations(Prestamo prestamo) {
        if (verifyIfEstudianteHasPrestamo(prestamo.getId_estudiante())) {
            throw new PrestamosException.PrestamosExceptionEstudianteHasPrestamo("El estudiante ya tiene un préstamo activo");
        }
        if (!verifyIfBookIsAvailable(prestamo.getId_libro())) {
            throw new PrestamosException.PrestamosExceptionBookIsAvailable("El libro no está disponible");
        }
        if (prestamo.getFecha_devolucion() != null && prestamo.getFecha_prestamo().isAfter(prestamo.getFecha_devolucion())) {
            throw new PrestamosException.PrestamosExceptionTimeError("La fecha de préstamo no puede ser después de la fecha de devolución");
        }
        if (!prestamo.getEstado().matches("Prestado|Vencido|Devuelto")) {
            throw new PrestamosException.PrestamosExceptionStateError("El estado solo puede ser Prestado, Vencido o Devuelto");
        }
    }

    /**
     * Verifies if a student has an active Prestamo.
     *
     * @param id_estudiante the ID of the student
     * @return true if the student has an active Prestamo, false otherwise
     */
    private boolean verifyIfEstudianteHasPrestamo(String id_estudiante) {
        //TODO: Implementar
        return false;
    }

    /**
     * Verifies if a book is available for Prestamo.
     *
     * @param id_libro the ID of the book
     * @return true if the book is available, false otherwise
     */
    private boolean verifyIfBookIsAvailable(String id_libro) {
        //TODO: Implementar
        return true;
    }

    /**
     * Retrieves all Prestamos.
     *
     * @return a list of all Prestamos
     */
    public List<Prestamo> getPrestamos() {
        return prestamoRepository.findAll();
    }

    /**
     * Retrieves all Prestamos with status "Prestado".
     *
     * @return a list of Prestamos with status "Prestado"
     */
    public List<Prestamo> getPrestamosWithStatusIsPrestado() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        List<Prestamo> prestamosPrestados = prestamos.stream()
                .filter(prestamo -> prestamo.getEstado().equals("Prestado"))
                .toList();
        return prestamosPrestados;
    }
}


