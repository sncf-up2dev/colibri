package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Page;
import fr.sncf.d2d.colibri.domain.common.PageSpecs;
import fr.sncf.d2d.colibri.domain.common.Service;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Service
public class UserService {

    private final Logger logger;
    private final UserRepository repository;
    private final PasswordEncryptor passwordEncryptor;

    public UserService(
            Logger logger,
            UserRepository repository,
            PasswordEncryptor passwordEncryptor
    ) {
        this.logger = logger;
        this.repository = repository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public AppUser retrieve(String id) {
        this.logger.info(() -> "Retrieving user with username %s.".formatted(id));
        return this.repository.retrieve(id).orElseThrow(NotFoundException::new);
    }

    public List<AppUser> retrieve() {
        this.logger.info("Retrieving all users.");
        return this.repository.retrieve();
    }

    public Page<AppUser> retrieve(PageSpecs pageSpecs) {
        this.logger.info("Retrieving user's page number %s.".formatted(pageSpecs.pageNumber()));
        return this.repository.retrieve(pageSpecs);
    }

    public void delete(String id) {
        this.logger.info(() -> "Deleting user with username %s.".formatted(id));
        this.repository.delete(id);
    }

    public AppUser update(String id, Consumer<AppUser> updater) {
        this.logger.info(() -> "Updating user with username %s.".formatted(id));
        AppUser user = this.repository.retrieve(id)
                .orElseThrow(() -> new NotFoundException("Username %s already exists".formatted(id)));
        updater.accept(user);
        if (user.getPassword() != null) {
            user.setPassword(this.passwordEncryptor.encode(user.getPassword()));
        }
        return this.repository.update(user);
    }

    public AppUser create(AppUser user) {
        this.logger.info("Creating new user.");
        return this.repository.create(user);
    }
}
