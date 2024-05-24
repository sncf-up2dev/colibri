package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelRepository;

import java.util.UUID;

public class InMemParcelRepository extends InMemCrudRepository<Parcel> implements ParcelRepository {

    @Override
    public Parcel create(Parcel entity) {
        if (entity.getId() != null) {
            throw new IllegalOperationException("Parcel ID cannot be provided on creation");
        }
        entity.setId(UUID.randomUUID().toString());
        return super.create(entity);
    }

    @Override
    protected String conflictMessage(String id) {
        return "Parcel with ID %s already exists".formatted(id);
    }

    @Override
    protected String notFoundMessage(String id) {
        return "Parcel with ID %s not found".formatted(id);
    }
}
