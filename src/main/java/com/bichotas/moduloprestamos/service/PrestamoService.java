package com.bichotas.moduloprestamos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.repository.PrestamoRepository;

import lombok.AllArgsConstructor;

/**
 * Service class for managing Prestamo (loan) operations.
 * This class provides methods to create, validate, and retrieve Prestamos.
 * It also includes methods to send email notifications and verify the availability of books and students' loan status.
 * 
 * @author Diego Cardenas
 * @author Sebastian Cardona
 * @author Zayra Gutierrez
 * @author Andres Serrato
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;

    /**
     * Creates a new Prestamo (loan) with the current date and time, performs validations,
     * and saves it to the repository.
     *
     * @param prestamo the Prestamo object to be created
     * @return the saved Prestamo object
     */
    public Prestamo createPrestamo(Prestamo prestamo) {
        prestamo.setFecha_prestamo(LocalDate.now());
        prestamo.setFecha_creacion(LocalDateTime.now());
        createPrestamoValidations(prestamo);
        return prestamoRepository.save(prestamo);
    }

    /**
     * Sends an email notification for the corresponding loan.
     * This method calls an API to send the emails related to the loan.
     * 
     */
    private void sendEmail(Prestamo prestamo) {
        //TODO: Implementar
    }

    /**
     * Validates the given Prestamo object to ensure it meets the necessary conditions
     * for creating a new loan.
     *
     * @param prestamo the Prestamo object to be validated
     * @throws PrestamosException.PrestamosExceptionEstudianteHasPrestamo if the student already has an active loan
     * @throws PrestamosException.PrestamosExceptionBookIsAvailable if the book is not available
     * @throws PrestamosException.PrestamosExceptionTimeError if the loan date is after the return date
     * @throws PrestamosException.PrestamosExceptionStateError if the state is not one of "Prestado", "Vencido", or "Devuelto"
     */
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


