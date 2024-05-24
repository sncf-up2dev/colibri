package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Service;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User retrieve(String id) {
        return this.repository.retrieve(id).orElseThrow(NotFoundException::new);
    }

    public List<User> retrieve() {
        return this.repository.retrieve();
    }

    public void delete(String id) {
        this.repository.delete(id);
    }

    public User update(String id, Consumer<User> updater) {
        User user = this.repository.retrieve(id)
                .orElseThrow(() -> new NotFoundException("Username %s already exists".formatted(id)));
        updater.accept(user);
        if (user.getPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }
        return this.repository.update(user);
    }

    public User create(User user) {
        return this.repository.create(user);
    }
}
