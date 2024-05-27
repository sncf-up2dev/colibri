package fr.sncf.d2d.colibri.domain.users;

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

    public User retrieve(String id) {
        this.logger.info(() -> "Retrieving user with username %s.".formatted(id));
        return this.repository.retrieve(id).orElseThrow(NotFoundException::new);
    }

    public List<User> retrieve() {
        this.logger.info("Retrieving all users.");
        return this.repository.retrieve();
    }

    public void delete(String id) {
        this.logger.info(() -> "Deleting user with username %s.".formatted(id));
        this.repository.delete(id);
    }

    public User update(String id, Consumer<User> updater) {
        this.logger.info(() -> "Updating user with username %s.".formatted(id));
        User user = this.repository.retrieve(id)
                .orElseThrow(() -> new NotFoundException("Username %s already exists".formatted(id)));
        updater.accept(user);
        if (user.getPassword() != null) {
            user.setPassword(this.passwordEncryptor.encode(user.getPassword()));
        }
        return this.repository.update(user);
    }

    public User create(User user) {
        this.logger.info("Creating new user.");
        return this.repository.create(user);
    }
}
