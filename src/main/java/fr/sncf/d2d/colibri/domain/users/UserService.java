package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
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

    public void update(String id, Consumer<User> updater) {
        User user = this.repository.retrieve(id).orElseThrow(NotFoundException::new);
        updater.accept(user);
        this.repository.update(user);
    }

    public User create(User user) {
        return this.repository.create(user);
    }
}
