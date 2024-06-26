package fr.sncf.d2d.colibri.persistence.backbone;

import fr.sncf.d2d.colibri.persistence.models.AppUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserEntityRepository extends ListPagingCurdRepository<AppUserEntity> {
}
