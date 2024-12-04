package com.bichotas.moduloprestamos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.entity.dto.DevolucionDTO;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.repository.PrestamoRepository;

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

    private static final String VENCIDO = "Vencido";
    private static final String DEVUELTO = "Devuelto";
    private final PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
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
     * @param idEstudiante the ID of the student
     * @return true if the student has an active Prestamo, false otherwise
     */
    private boolean verifyIfEstudianteHasPrestamo(String idEstudiante) {
        /*List<Prestamo> prestamos = prestamoRepository.findByIdEstudiante(idEstudiante);
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getEstado().equals("Prestado")) {
                return true;
            }
        }*/
        return false;
    }

    /**
     * Retrieves a list of Prestamo objects based on the provided estado.
     * If the estado is null, it retrieves all Prestamo objects.
     * If the estado is "Prestado", it retrieves all Prestamo objects that are currently lent out.
     * If the estado is "Vencido", it retrieves all Prestamo objects that are overdue.
     * If the estado is "Devuelto", it retrieves all Prestamo objects that have been returned.
     *
     * @param estado the state of the Prestamo objects to retrieve. It can be "Prestado", "Vencido", or "Devuelto".
     * @return a list of Prestamo objects matching the specified estado.
     * @throws PrestamosException.PrestamosExceptionStateError if the estado is not one of "Prestado", "Vencido", or "Devuelto".
     */
    public List<Prestamo> getPrestamos(String estado) {
        if (estado == null) {
            return getPrestamos();
        } else {
            switch (estado) {
                case "Prestado":
                    return getPrestamosPrestado();
                case "Vencido":
                    return getPrestamosVencido();
                case "Devuelto":
                    return getPrestamosDevuelto();
                default:
                    throw new PrestamosException.PrestamosExceptionStateError("El estado solo puede ser Prestado, Vencido o Devuelto");
            }
        }
    }

    /**
     * Retrieves a list of all Prestamo entities from the repository.
     *
     * @return a List of Prestamo objects.
     */
    private List<Prestamo> getPrestamos() {
        return prestamoRepository.findAll();
    }

    /**
     * Retrieves a list of loans that are currently in the "Prestado" (loaned) state.
     *
     * @return a list of {@link Prestamo} objects that have the status "Prestado".
     */
    private List<Prestamo> getPrestamosPrestado() {
        return prestamoRepository.findByEstado("Prestado");
    }

    /**
     * Retrieves a list of loans that are overdue.
     *
     * @return a list of {@link Prestamo} objects with the status "Vencido".
     */
    private List<Prestamo> getPrestamosVencido() {
        return prestamoRepository.findByEstado(VENCIDO);
    }

    /**
     * Retrieves a list of loans that have been returned.
     *
     * @return a list of {@link Prestamo} objects with the status "Devuelto".
     */
    private List<Prestamo> getPrestamosDevuelto() {
        return prestamoRepository.findByEstado(DEVUELTO);
    }


    /**
     * Retrieves a prestamo by its ID.
     *
     * @param id the ID of the prestamo
     * @return the prestamo with the given ID
     */
    public Prestamo getPrestamoById(String id) {
        return prestamoRepository.findById(id).orElseThrow(() ->
                new PrestamosException.PrestamosExceptionPrestamoIdNotFound("El préstamo con el id " + id + " no existe"));
    }

    /**
     * retrieves all prestamos by the isbn of the book
     *
     * @param isbn the isbn of the book
     * @return the prestamos with the given isbn or throws an exception if the book does not exist
     */
    public List<Prestamo> getPrestamosByIsbn(String isbn) {
        List<Prestamo> prestamosFiltrados =
                getPrestamos().stream()
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
        List<Prestamo> prestamos =  prestamoRepository.findByIdEstudiante(id);
        if (prestamos.isEmpty()) {
            throw new PrestamosException.PrestamosExceptionEstudianteHasNotPrestamo("El estudiante con el id " + id + " no tiene préstamos o no existe.");
        }
        return prestamos;
    }

    /**
     * Delete a prestamo by its ID if it has not been returned or is not overdue.
     *
     * @param id the ID of the prestamo
     * @return the prestamo deleted
     */
    public Prestamo deletePrestamoById(String id) {
        Prestamo prestamo = getPrestamoById(id);
        if (prestamo.getEstado().equals(DEVUELTO)) {
            throw new PrestamosException.PrestamosExceptionStateError("El préstamo ya ha sido devuelto");
        } else if (prestamo.getEstado().equals(VENCIDO)) {
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
        /*if ((VENCIDO.equals(prestamo.getEstado()) || DEVUELTO.equals(prestamo.getEstado())) && !updates.containsKey("historial_estado")) {
            throw new IllegalArgumentException("No se puede actualizar el préstamo en estado vencido o devuelto, excepto el historial del ejemplar");
        }*/
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

    public Prestamo devolverPrestamo(String prestamoId, String estado) {
        Prestamo prestamo = getPrestamoById(prestamoId);
        prestamo.setEstado("Devuelto");
        prestamo.setHistorialEstado(estado);
        prestamo.setFechaDevolucion(LocalDate.now());
        /*
        //TODO: Implementar se implementa la peticion a la api de envio de correos
        prestamoRepository.save(prestamo);

        //boolean estadoHistory = getEstadoHistory(prestamo.getIdLibro(), estado);

        DevolucionDTO devolucionDTO = DevolucionDTO.builder()
                .userId(prestamo.getIdEstudiante())
                .emailGuardian("")
                .bookId(prestamo.getIdLibro())
                .bookName("")
                //.loanReturn(estadoHistory)
                .build();
        */
        return prestamo;
    }

    /*
    private boolean getEstadoHistory(String idLibro, String estado) {
        List<Prestamo> prestamos = prestamoRepository.getPrestamosByIdLibro(idLibro);
        Prestamo prestamo;
        if (prestamos.isEmpty()) return false;
        prestamo = prestamos.get(0);
        for (int i = 1; i < prestamos.size(); i++) {
            if (prestamos.get(i).getFechaPrestamo().isAfter(prestamo.getFechaPrestamo())) {
                prestamo = prestamos.get(i);
            }
        }
        if (prestamo.getHistorialEstado().equals(estado)) {
            return false;
        } else {
            return true;
        }
    }*/
}


