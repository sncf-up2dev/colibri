package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.CrudRepository;
import fr.sncf.d2d.colibri.domain.common.Entity;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemCrudRepository<T extends Entity> implements CrudRepository<T> {

    Map<String, T> data = new HashMap<>();

    @Override
    public Optional<T> retrieve(String id) {
        Objects.requireNonNull(id, "Argument id cannot be null");
        return this.data.entrySet().stream()
                .filter(e -> id.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .findAny();
    }

    @Override
    public List<T> retrieve() {
        return List.copyOf(data.values());
    }

    @Override
    public T create(T entity) {
        this.checkForConflict(entity.getId());
        this.data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(String id) {
        this.checkForNotFound(id);
        this.data.remove(id);
    }

    @Override
    public T update(T entity) {
        this.checkForNotFound(entity.getId());
        this.data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T save(T entity) {
        return entity.getId() == null ? this.create(entity) : this.update(entity);
    }

    protected String conflictMessage(String id) {
        return "Entity with ID %s already".formatted(id);
    }

    protected void checkForConflict(String id) {
        if (!this.data.containsKey(id)) {
            return;
        }
        throw new ConflictException(this.conflictMessage(id));
    }

    protected String notFoundMessage(String id) {
        return "Entity with ID %s not found".formatted(id);
    }

    protected void checkForNotFound(String id) {
        if (this.data.containsKey(id)) {
            return;
        }
        throw new NotFoundException(notFoundMessage(id));
    }
}
