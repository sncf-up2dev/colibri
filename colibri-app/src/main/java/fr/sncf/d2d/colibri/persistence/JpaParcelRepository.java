package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelRepository;
import fr.sncf.d2d.colibri.persistence.models.ParcelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaParcelRepository
        extends JpaCrudRepository<Parcel, ParcelEntity>
        implements ParcelRepository {

    protected JpaParcelRepository(CrudRepository<ParcelEntity, String> crudRepository) {
        super(crudRepository);
    }

    @Override
    public Parcel create(Parcel model) {
        if (model.getId() != null) {
            throw new IllegalOperationException("Parcel ID cannot be provided on creation");
        }
        model.setId(UUID.randomUUID().toString());
        return this.toModel(this.entityRepository.save(toEntity(model)));
    }

    @Override
    public Parcel toModel(ParcelEntity entity) {
        return entity.toParcel();
    }

    @Override
    public ParcelEntity toEntity(Parcel model) {
        return ParcelEntity.from(model);
    }
}
