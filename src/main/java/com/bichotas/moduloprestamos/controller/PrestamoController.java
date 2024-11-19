package com.bichotas.moduloprestamos.controller;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.exception.PrestamosException;
import com.bichotas.moduloprestamos.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping("/prestamos")
@Tag(
        name = "Prestamos",
        description = "Operaciones relacionadas con los prestamos de libros"
)
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;


    @PostMapping
    @Operation(
            summary = "Crear un nuevo préstamo",
            description = "Crea un nuevo préstamo de un libro para un estudiante con los detalles proporcionados.",
            tags = {"Prestamos"},
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
                        "id_estudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                        "id_libro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                        "fecha_devolucion": "2025-09-18",
                        "estado": "Prestado",
                        "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                        "historial_estado": "Estado de como de presta",
                        "creado_by": "5f5b3b3b1f1b3b5f5b3b3b1f"
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
                            "id_estudiante": "5f5b3b3b1f1b3b5f5b3b3b1f",
                            "id_libro": "5f5b3b3b1f1b3b5f5b3b3b1f",
                            "fecha_prestamo": "2024-11-19",
                            "fecha_devolucion": "2025-09-18",
                            "estado": "Prestado",
                            "observaciones": "El estudiante se compromete a devolver el libro en la fecha acordada",
                            "historial_estado": "Estado de como de presta",
                            "creado_by": "5f5b3b3b1f1b3b5f5b3b3b1f",
                            "fecha_creacion": "2024-11-19T14:30:00"
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


    @GetMapping
    @Operation(
            summary = "Obtener todos los prestamos",
            description = "Obtiene todos los prestamos registrados en el sistema",
            tags = {"Prestamos"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Prestamos obtenidos correctamente"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> getPrestamos() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("prestamos", prestamoService.getPrestamos()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @GetMapping("/prestamos-prestados")
    public ResponseEntity<?> getPrestamosWithStatusIsPrestado() {
        //TODO: Implementar
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("Prestamos", prestamoService.getPrestamosWithStatusIsPrestado()));
    }

    @GetMapping("/libros-disponibles")
    public ResponseEntity<?> getLibrosDisponibles(){
        //TODO: Implementar
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("libros", "libros disponibles"));
    }
}
