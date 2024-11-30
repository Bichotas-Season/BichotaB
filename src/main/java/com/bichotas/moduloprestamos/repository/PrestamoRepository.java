package com.bichotas.moduloprestamos.repository;

import com.bichotas.moduloprestamos.entity.Prestamo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Prestamo} entities in MongoDB.
 * Extends {@link MongoRepository} to provide CRUD operations and additional query methods.
 */
@Repository
public interface PrestamoRepository extends MongoRepository<Prestamo, ObjectId> {
    List<Prestamo> getPrestamosByIdLibro(String idLibro);
}