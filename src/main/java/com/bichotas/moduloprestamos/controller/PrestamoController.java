package com.bichotas.moduloprestamos.controller;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/v1.0/prestamos")
@Tag(
        name = "Prestamos",
        description = "Operaciones relacionadas con los prestamos de libros"
)
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    /**
     * create a new prestamo with the details provided
     * @param prestamo
     * @return
     */
    @PostMapping
    @Operation(
            method = "POST",
            summary = "Crear un nuevo préstamo",
            description = "Crea un nuevo préstamo de un libro para un estudiante con los detalles proporcionados.",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "prestamo",
                            description = "Detalles del préstamo a crear",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Prestamo.class)
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detalles del préstamo a crear",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Prestamo.class),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Ejemplo de préstamo",
                                            value = """
                                                    {
                                                        "idEstudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                        "idLibro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                        "fechaDevolucion": "2025-09-18",
                                                        "estado": "Prestado",
                                                        "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                                                        "historialEstado": "Estado de como de presta",
                                                        "creadoBy": "5f5b3b3b1f1b3b5f5b3b3b1f"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Préstamo creado exitosamente",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Respuesta exitosa",
                                            value = """
                                                    {
                                                        "prestamo": {
                                                            "id": "64759fa2edbdee1a2c7b4e1f",
                                                            "idEstudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                            "idLibro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                            "fechaPrestamo": "2024-11-19",
                                                            "fechaDevolucion": "2025-09-18",
                                                            "estado": "Prestado",
                                                            "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                                                            "historialEstado": "Estado de como de presta",
                                                            "creadoBy": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                            "fechaCreacion": "2024-11-19T14:30:00"
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Error de validación o negocio",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de validación",
                                            value = """
                                                    {
                                                        "error": "El estudiante ya tiene un préstamo activo"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error inesperado en el servidor",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error del servidor",
                                            value = """
                                                    {
                                                        "error": "Error inesperado: NullPointerException"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> createPrestamo(@Valid @RequestBody Prestamo prestamo) {
        try {
            Prestamo prestamoSaved = prestamoService.createPrestamo(prestamo);
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("prestamo", prestamoSaved));
        } catch (PrestamosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error inesperado: " + e.getMessage()));
        }
    }

    /**
     * get all prestamos in the system
     * @return
     */
    @GetMapping
    @Operation(
            method = "GET",
            summary = "Obtener todos los préstamos",
            description = "Obtiene todos los préstamos registrados en el sistema o los filtra según el estado proporcionado.",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "estado",
                            description = "Estado de los préstamos a obtener. Puede ser 'Prestado', 'Vencido', o 'Devuelto'.",
                            required = false,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Préstamos obtenidos correctamente",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Prestamo.class),
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Lista de préstamos",
                                            value = """
                                                    {
                                                        "prestamos": [
                                                            {
                                                                "id": "64759fa2edbdee1a2c7b4e1f",
                                                                "idEstudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "idLibro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "fechaPrestamo": "2024-11-19",
                                                                "fechaDevolucion": "2025-09-18",
                                                                "estado": "Prestado",
                                                                "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                                                                "historialEstado": "Estado de como de presta",
                                                                "creadoBy": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "fechaCreacion": "2024-11-19T14:30:00"
                                                            },
                                                            {
                                                                "id": "64759fa2edbdee1a2c7b4e1f",
                                                                "idEstudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "idLibro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "fechaPrestamo": "2024-11-19",
                                                                "fechaDevolucion": "2025-09-18",
                                                                "estado": "Prestado",
                                                                "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                                                                "historialEstado": "Estado de como de presta",
                                                                "creadoBy": "5f5b3b3b1f1b3b5f5b3b3b1f",
                                                                "fechaCreacion": "2024-11-19T14:30:00"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Estado no válido proporcionado",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de validación",
                                            value = """
                                                    {
                                                        "error": "El estado proporcionado no es válido. Debe ser 'Prestado', 'Vencido', o 'Devuelto'"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error del servidor",
                                            value = """
                                                    {
                                                        "error": "Error inesperado: NullPointerException"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> getPrestamos(@RequestParam(value = "estado", required = false) String estado) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("prestamos", prestamoService.getPrestamos(estado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     *Get the details of a specific prestamos using its ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener préstamo por ID",
            description = "Obtiene los detalles de un préstamo específico usando su ID",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "ID del préstamo a buscar",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Préstamo encontrado correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Préstamo no encontrado"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> getPrestamoById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("prestamo", prestamoService.getPrestamoById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Get all prestamos associated with a specific book using its ISBN
     * @param isbn
     * @return
     */
    @GetMapping("/por-libro/{isbn}")
    @Operation(
            summary = "Obtener préstamos por ISBN del libro",
            description = "Obtiene todos los préstamos asociados a un libro específico usando su ISBN",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "isbn",
                            description = "ISBN del libro",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lista de préstamos encontrada correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> getPrestamosByIsbn(@PathVariable String isbn) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("prestamos", prestamoService.getPrestamosByIsbn(isbn)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * get prestamos by id estudiante
     * @param id
     * @return
     */
    @GetMapping("/por-estudiante/{id}")
    @Operation(
            summary = "Obtener préstamos por ID de estudiante",
            description = "Obtiene todos los préstamos asociados a un estudiante específico",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "ID del estudiante",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lista de préstamos del estudiante encontrada correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> getPrestamosByEstudiante(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("prestamos", prestamoService.getPrestamosByIdEstudiante(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * delete prestamo by id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/delete")
    @Operation(
            summary = "Eliminar préstamo",
            description = "Elimina un préstamo específico usando su ID",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "ID del préstamo a eliminar",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Préstamo eliminado correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Préstamo no encontrado"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> deletePrestamo(@PathVariable String id) {
        try {
            prestamoService.deletePrestamoById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Prestamo eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    /**
     * update prestamo by id except if the prestamo is vencido or devuelto
     * @param id
     * @param updates
     * @return
     */
    @PatchMapping("/{id}/update")
    @Operation(
            summary = "Actualizar un atributo del préstamo",
            description = "Actualiza un atributo específico del préstamo, excepto si el préstamo está en estado de vencido o devuelto",
            tags = {"Prestamos"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "ID del préstamo a actualizar",
                            required = true,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Préstamo actualizado correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Solicitud incorrecta"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Préstamo no encontrado"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> updatePrestamo(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            prestamoService.updatePrestamo(id, updates);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Prestamo actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> devolverPrestamo(@PathVariable String prestamoId, @PathVariable String estado){
        try {
            prestamoService.devolverPrestamo(prestamoId, estado);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Prestamo devuelto correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
