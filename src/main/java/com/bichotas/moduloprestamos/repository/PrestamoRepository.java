package com.bichotas.moduloprestamos.repository;

import com.bichotas.moduloprestamos.entity.Prestamo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Prestamo} entities in MongoDB.
 * Extends {@link MongoRepository} to provide CRUD operations and additional query methods.
 */
@Repository
public interface PrestamoRepository extends MongoRepository<Prestamo, String> {
    List<Prestamo> getPrestamosByIdLibro(String idLibro);
    List<Prestamo> findByEstado(@NotBlank(message = "El estado no puede estar vacío") @Pattern(regexp = "Prestado|Vencido|Devuelto", message = "El estado solo puede ser Prestado, Vencido o Devuelto") String estado);
    List<Prestamo> findByIdEstudiante(@NotBlank(message = "El id del estudiante no puede estar vacío") String idEstudiante);
}