package fr.sncf.d2d.colibri.domain.common;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Entity> {
    Optional<T> retrieve(String id);
    List<T> retrieve();
    T create(T entity);
    void delete(String id);
    T update(T entity);
    T save(T entity);
}
