package com.student.dao;

import java.util.List;

/**
 * Generic Base Data Access Object interface.
 * Demonstrates:
 * - Generics in Java (<T>)
 * - Interface-based architecture
 * - Loose coupling in MVC / DAO design
 *
 * @param <T> Model type managed by the DAO
 */
public interface BaseDAO<T> {

    /**
     * Inserts a new record into the database.
     * @param entity entity object to insert
     * @return true if successful, false otherwise
     */
    boolean add(T entity);

    /**
     * Updates an existing record in the database.
     * @param entity entity object containing updated attributes
     * @return true if successful, false otherwise
     */
    boolean update(T entity);

    /**
     * Deletes a record by its identifier.
     * @param key identifier (e.g. primary key or code)
     * @return true if deleted, false otherwise
     */
    boolean delete(Object key);

    /**
     * Retrieves an entity by its identifier.
     * @param key identifier
     * @return entity object or null if not found
     */
    T getById(Object key);

    /**
     * Retrieves all records for this entity.
     * @return List of entities
     */
    List<T> getAll();
}
