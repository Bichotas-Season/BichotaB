package com.bichotas.moduloprestamos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private List<Prestamo> prestamos;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.prestamos = prestamoRepository.findAll();
    }

    /**
     * Creates a new Prestamo (loan) with the current date and time, performs validations,
     * and saves it to the repository.
     *
     * @param prestamo the Prestamo object to be created
     * @return the saved Prestamo object
     */
    public Prestamo createPrestamo(Prestamo prestamo) {
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaCreacion(LocalDateTime.now());
        createPrestamoValidations(prestamo);
        sendEmail(prestamo);
        return prestamoRepository.save(prestamo);
    }

    /**
     * Sends an email notification for the corresponding loan.
     * This method calls an API to send the emails related to the loan.
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
     * @throws PrestamosException.PrestamosExceptionBookIsAvailable       if the book is not available
     * @throws PrestamosException.PrestamosExceptionTimeError             if the loan date is after the return date
     * @throws PrestamosException.PrestamosExceptionStateError            if the state is not one of "Prestado", "Vencido", or "Devuelto"
     */
    private void createPrestamoValidations(Prestamo prestamo) {
        if (verifyIfEstudianteHasPrestamo(prestamo.getIdEstudiante())) {
            throw new PrestamosException.PrestamosExceptionEstudianteHasPrestamo("El estudiante ya tiene un préstamo activo");
        }
        if (!verifyIfBookIsAvailable(prestamo.getIdLibro())) {
            throw new PrestamosException.PrestamosExceptionBookIsAvailable("El libro no está disponible");
        }
        if (prestamo.getFechaDevolucion() != null && prestamo.getFechaPrestamo().isAfter(prestamo.getFechaDevolucion())) {
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
        return prestamos;
    }

    /**
     * Retrieves all Prestamos with status "Prestado".
     *
     * @return a list of Prestamos with status "Prestado"
     */
    public List<Prestamo> getPrestamosWithStatusIsPrestado() {
        return prestamos.stream()
                .filter(prestamo -> prestamo.getEstado().equals("Prestado"))
                .toList();
    }

    /**
     * Retrieves a prestamo by its ID.
     *
     * @param id the ID of the prestamo
     * @return the prestamo with the given ID
     */
    public Prestamo getPrestamoById(String id) {
        return prestamos.stream()
                .filter(p -> p.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new PrestamosException.PrestamosExceptionPrestamoIdNotFound("El préstamo con el id " + id + " no existe"));
    }

    /**
     * retrieves all prestamos by the isbn of the book
     *
     * @param isbn the isbn of the book
     * @return the prestamos with the given isbn or throws an exception if the book does not exist
     */
    public List<Prestamo> getPrestamosByIsbn(String isbn) {
        List<Prestamo> prestamosFiltrados = prestamos.stream()
                .filter(p -> p.getIdLibro().equals(isbn))
                .toList();

        if (prestamosFiltrados.isEmpty()) {
            throw new PrestamosException.PrestamosExceptionBookIsAvailable("El libro con el ISBN " + isbn + " no ha sido prestado o no existe");
        }

        return prestamosFiltrados;
    }

    /**
     * Retrieves all prestamos by the id of the student
     *
     * @param id the id of the student
     * @return the prestamos with the given id or throws an exception if the student does not exist
     */
    public List<Prestamo> getPrestamosByIdEstudiante(String id) {
        List<Prestamo> prestamosFiltrados = prestamos.stream()
                .filter(p -> p.getIdEstudiante().equals(id))
                .toList();

        if (prestamosFiltrados.isEmpty()) {
            throw new PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo("El estudiante con el id " + id + " no tiene préstamos o no existe.");
        }

        return prestamosFiltrados;
    }

    /**
     * Delete a prestamo by its ID if it has not been returned or is not overdue.
     *
     * @param id the ID of the prestamo
     * @return the prestamo deleted
     */
    public Prestamo deletePrestamoById(String id) {
        Prestamo prestamo = getPrestamoById(id);
        if (prestamo.getEstado().equals("Devuelto")) {
            throw new PrestamosException.PrestamosExceptionStateError("El préstamo ya ha sido devuelto");
        } else if (prestamo.getEstado().equals("Vencido")) {
            throw new PrestamosException.PrestamosExceptionStateError("El préstamo está vencido");
        } else {
            prestamoRepository.deleteById(prestamo.getId());
            return prestamo;
        }

    }

    /**
     * Updates the specified Prestamo (loan) with the provided updates.
     *
     * @param id      the ID of the Prestamo to be updated
     * @param updates a map containing the fields to be updated and their new values
     * @throws IllegalArgumentException if the Prestamo is in "vencido" or "devuelto" state and the updates do not contain "historial_estado",
     *                                  or if an invalid attribute is provided in the updates map
     */
    public void updatePrestamo(String id, Map<String, Object> updates) {
        Prestamo prestamo = getPrestamoById(id);

        if ("vencido".equals(prestamo.getEstado()) || "devuelto".equals(prestamo.getEstado())) {
            if (!updates.containsKey("historial_estado")) {
                throw new IllegalArgumentException("No se puede actualizar el préstamo en estado vencido o devuelto, excepto el historial del ejemplar");
            }
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "observaciones":
                    prestamo.setObservaciones((String) value);
                    break;
                case "estado":
                    prestamo.setEstado((String) value);
                    break;
                case "fecha_devolucion":
                    if (value instanceof String) {
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        LocalDateTime fechaDevolucion = LocalDateTime.parse((String) value, formatter);
                        prestamo.setFechaDevolucion(LocalDate.from(fechaDevolucion));
                    } else if (value instanceof LocalDateTime) {
                        prestamo.setFechaDevolucion(LocalDate.from((LocalDateTime) value));
                    } else {
                        throw new IllegalArgumentException("Formato de fecha_devolucion no válido");
                    }
                    break;
                case "historial_estado":
                    prestamo.setHistorialEstado((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Atributo no válido: " + key);
            }
        });

        prestamoRepository.save(prestamo);
    }
}


