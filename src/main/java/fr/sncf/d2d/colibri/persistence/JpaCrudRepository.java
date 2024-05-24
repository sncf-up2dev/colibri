package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.BaseCrudRepository;
import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.Model;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@NoRepositoryBean
public interface JpaCrudRepository<M extends Model, E>
        extends BaseCrudRepository<M>, CrudRepository<E, String> {

    M toModel(E entity);

    E toEntity(M model);

    @Override
    default Optional<M> retrieve(String id) {
        return this.findById(id).map(this::toModel);
    }

    @Override
    default List<M> retrieve() {
        return StreamSupport.stream(this.findAll().spliterator(), false)
                .map(this::toModel)
                .toList();
    }

    @Override
    default M create(M model) {
        if (this.existsById(model.getId())) {
            throw new ConflictException("Model with ID %s already exists.".formatted(model.getId()));
        }
        return this.toModel(this.save(toEntity(model)));
    }

    @Override
    default void delete(String id) {
        E entity = this.findById(id).orElseThrow(() ->
                new NotFoundException("No model found with ID %s.".formatted(id)));
        this.delete(entity);
    }

    @Override
    default M update(M model) {
        return this.toModel(this.save(this.toEntity(model)));
    }

    @Override
    default M save(M model) {
        return model.getId() == null ? this.create(model) : this.update(model);
    }
}
