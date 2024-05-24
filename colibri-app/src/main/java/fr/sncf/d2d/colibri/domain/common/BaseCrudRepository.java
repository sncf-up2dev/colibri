package fr.sncf.d2d.colibri.domain.common;

import java.util.List;
import java.util.Optional;

public interface BaseCrudRepository<T extends Model> {
    Optional<T> retrieve(String id);
    List<T> retrieve();
    T create(T model);
    void delete(String id);
    T update(T model);
    T save(T model);
}
