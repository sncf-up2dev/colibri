package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.users.User;
import fr.sncf.d2d.colibri.domain.users.UserRepository;
import fr.sncf.d2d.colibri.persistence.models.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository
        extends JpaCrudRepository<User, UserEntity>, UserRepository {

    @Override
    default User toModel(UserEntity entity) {
        return entity.toUser();
    }

    @Override
    default UserEntity toEntity(User model) {
        return UserEntity.from(model);
    }
}
