package com.bichotas.moduloprestamos.repository;

import com.bichotas.moduloprestamos.entity.Prestamo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Prestamo} entities in MongoDB.
 * Extends {@link MongoRepository} to provide CRUD operations and additional query methods.
 */
@Repository
public interface PrestamoRepository extends MongoRepository<Prestamo, ObjectId> {

}