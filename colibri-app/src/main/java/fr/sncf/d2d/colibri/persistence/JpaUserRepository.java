package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserRepository;
import fr.sncf.d2d.colibri.persistence.models.AppUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository
        extends JpaCrudRepository<AppUser, AppUserEntity>, UserRepository {

    @Override
    default AppUser toModel(AppUserEntity entity) {
        return entity.toUser();
    }

    @Override
    default AppUserEntity toEntity(AppUser model) {
        return AppUserEntity.from(model);
    }
}
