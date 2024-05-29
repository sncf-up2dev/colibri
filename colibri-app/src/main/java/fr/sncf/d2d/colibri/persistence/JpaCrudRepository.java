package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.BaseCrudRepository;
import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.Model;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.persistence.errors.MapException;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@MapException
public abstract class JpaCrudRepository<M extends Model, E>
        implements BaseCrudRepository<M> {

    protected final CrudRepository<E, String> entityRepository;

    protected JpaCrudRepository(
            CrudRepository<E, String> entityRepository
    ) {
        this.entityRepository = entityRepository;
    }

    protected abstract M toModel(E entity);

    protected abstract E toEntity(M model);

    @Override
    public Optional<M> retrieve(String id) {
        return this.entityRepository.findById(id).map(this::toModel);
    }

    @Override
    public List<M> retrieve() {
        return StreamSupport.stream(this.entityRepository.findAll().spliterator(), false)
                .map(this::toModel)
                .toList();
    }

    @Override
    public M create(M model) {
        if (this.entityRepository.existsById(model.getId())) {
            throw new ConflictException("Model with ID %s already exists.".formatted(model.getId()));
        }
        return this.toModel(this.entityRepository.save(toEntity(model)));
    }

    @Override
    public void delete(String id) {
        E entity = this.entityRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No model found with ID %s.".formatted(id)));
        this.entityRepository.delete(entity);
    }

    @Override
    public M update(M model) {
        return this.toModel(this.entityRepository.save(this.toEntity(model)));
    }

    @Override
    public M save(M model) {
        return model.getId() == null ? this.create(model) : this.update(model);
    }
}
