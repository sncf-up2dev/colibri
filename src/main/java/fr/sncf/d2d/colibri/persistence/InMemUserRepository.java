package fr.sncf.d2d.colibri.persistence;

import fr.sncf.d2d.colibri.domain.users.User;
import fr.sncf.d2d.colibri.domain.users.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemUserRepository extends InMemCrudRepository<User> implements UserRepository {

    @Override
    protected String conflictMessage(String id) {
        return "Username %s already exists".formatted(id);
    }

    @Override
    protected String notFoundMessage(String id) {
        return "Username %s not found".formatted(id);
    }
}
