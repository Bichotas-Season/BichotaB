package com.bichotas.moduloprestamos.entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class is in charge of representing the prestamo entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document(collection = "prestamo")
public class Prestamo {
    @Id
    private ObjectId id;

    @NotBlank(message = "El id del estudiante no puede estar vacío")
    private String id_estudiante;

    @NotBlank(message = "El id del libro no puede estar vacío")
    private String id_libro;

    private LocalDate fecha_prestamo;

    private LocalDate fecha_devolucion;

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(regexp = "Prestado|Vencido|Devuelto", message = "El estado solo puede ser Prestado, Vencido o Devuelto")
    private String estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String observaciones;

    @CreatedDate
    private LocalDateTime fecha_creacion;

    @Size(max = 500, message = "El historial de estado no puede exceder los 500 caracteres")
    private String historial_estado;

    @NotNull(message = "El usuario que creó el préstamo no puede ser nulo")
    private String creado_by;
}