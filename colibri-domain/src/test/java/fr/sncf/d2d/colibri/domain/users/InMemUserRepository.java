package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.InMemCrudRepository;

public class InMemUserRepository extends InMemCrudRepository<AppUser> implements UserRepository {

    @Override
    protected String conflictMessage(String id) {
        return "Username %s already exists".formatted(id);
    }

    @Override
    protected String notFoundMessage(String id) {
        return "Username %s not found".formatted(id);
    }
}
