package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.domain.parcels.Parcel;
import fr.sncf.d2d.colibri.domain.parcels.ParcelRepository;
import fr.sncf.d2d.colibri.persistence.models.ParcelEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaParcelRepository
        extends JpaCrudRepository<Parcel, ParcelEntity>, ParcelRepository {

    @Override
    default Parcel create(Parcel model) {
        if (model.getId() != null) {
            throw new IllegalOperationException("Parcel ID cannot be provided on creation");
        }
        model.setId(UUID.randomUUID().toString());
        try {
            return this.toModel(this.save(toEntity(model)));
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Entity not found", e);
        } catch (DataAccessException e) {
            if (e.getCause() instanceof EntityNotFoundException) {
                throw new NotFoundException("Entity not found", e.getCause());
            }
            throw e;
        }
    }

    @Override
    default Parcel toModel(ParcelEntity entity) {
        return entity.toParcel();
    }

    @Override
    default ParcelEntity toEntity(Parcel model) {
        return ParcelEntity.from(model);
    }
}
