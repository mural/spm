package com.spm.common.repository;

import com.spm.common.domain.EntityInterface;

import java.util.Collection;
import java.util.List;

/**
 * Interface that all repositories must adhere to. It provides basic repository functionality.
 *
 * @param <T> This is a parameterized interface.
 */
public interface Repository<T extends EntityInterface> {

    /**
     * Retrieves an {@link Entity} from the repository according to an id.
     *
     * @param id the id for the {@link Entity} to retrieve
     * @return the {@link Entity} retrieved.
     * @throws ObjectNotFoundException in case no {@link Entity} with the given id is not found
     */
    public T get(Long id) throws ObjectNotFoundException;

    /**
     * Adds an entity to the repository.
     *
     * @param entity The {@link Entity} to add.
     */
    public void add(T entity);

    /**
     * Adds a collection of entities to the repository.
     *
     * @param entities The {@link Entity}s to add.
     */
    public void addAll(Collection<T> entities);

    /**
     * Removes an entity from the repository.
     *
     * @param entity The {@link Entity} to remove
     */
    public void remove(T entity);

    /**
     * Removes all the {@link Entity}s that the repository has.
     */
    public void removeAll();

    /**
     * Obtains a list containing all the {@link Entity}s in the repository
     *
     * @return the list of {@link Entity}s
     */
    public List<T> getAll();

    /**
     * Obtains a list containing the {@link Entity}s in the repository
     *
     * @param entity
     * @return the list of {@link Entity}s
     */
    public List<T> get(T entity);

    /**
     * Removes the {@link Entity} with the id
     *
     * @param id The {@link Entity} id to be removed
     */
    public void remove(Long id);

}
