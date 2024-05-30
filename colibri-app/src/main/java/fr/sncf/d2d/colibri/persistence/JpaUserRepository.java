package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserRepository;
import fr.sncf.d2d.colibri.persistence.backbone.ListPagingCurdRepository;
import fr.sncf.d2d.colibri.persistence.models.AppUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository
        extends JpaCrudRepository<AppUser, AppUserEntity>
        implements UserRepository {

    protected JpaUserRepository(ListPagingCurdRepository<AppUserEntity> crudRepository) {
        super(crudRepository);
    }

    @Override
    public AppUser toModel(AppUserEntity entity) {
        return entity.toUser();
    }

    @Override
    public AppUserEntity toEntity(AppUser model) {
        return AppUserEntity.from(model);
    }
}
