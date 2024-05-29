package fr.sncf.d2d.colibri.persistence.backbone;

import fr.sncf.d2d.colibri.persistence.models.AppUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserEntityRepository extends CrudRepository<AppUserEntity, String> {
}
