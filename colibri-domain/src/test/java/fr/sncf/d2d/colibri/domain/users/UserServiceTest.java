package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Base64PasswordEncryptor;
import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    UserRepository repository = new InMemUserRepository();
    UserService sut = new UserService(
            Logger.getAnonymousLogger(),
            repository,
            new Base64PasswordEncryptor()
    );

    @Test
    void retrieveOne() {
        // Given
        User user = randomUser();
        this.sut.create(user);
        // When
        User retrieved = this.sut.retrieve(user.getUsername());
        // Then
        assertThat(retrieved)
                .usingComparator(this::compareUsers)
                .isEqualTo(user);
    }

    @Test
    void retrieveOneNotFound() {
        assertThatThrownBy(() -> this.sut.retrieve("not_an_id"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testRetrieveAll() {
        // Given
        List<User> user = Stream.generate(this::randomUser).limit(10).toList();
        user.forEach(this.sut::create);
        // When
        List<User> retrieved = this.sut.retrieve();
        // Then
        assertThat(retrieved)
                .usingComparatorForType(this::compareUsers, User.class)
                .isSubsetOf(user);
    }

    @Test
    void delete() {
        // Given
        User user = randomUser();
        this.sut.create(user);
        // When
        this.sut.delete(user.getUsername());
        // Then
        assertThatThrownBy(() -> this.sut.retrieve(user.getUsername()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteNotFound() {
        assertThatThrownBy(() -> this.sut.delete("not_an_id"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update() {
        // Given
        User user = randomUser();
        this.sut.create(user);
        // When
        this.sut.update(user.getUsername(), u -> u.setRole(Role.POSTMAN));
        // Then
        User retrieved = this.sut.retrieve(user.getUsername());
        assertThat(retrieved.getRole()).isEqualTo(Role.POSTMAN);
    }

    @Test
    void updateNotFound() {
        assertThatThrownBy(() -> this.sut.update("not_an_id", ignore -> {}))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create() {
        // Given
        User user = randomUser();
        // When
        User created = this.sut.create(user);
        // Then
        assertThat(created)
                .usingComparator(this::compareUsers)
                .isEqualTo(user);
        User retrieved = this.sut.retrieve(user.getUsername());
        assertThat(retrieved)
                .usingComparator(this::compareUsers)
                .isEqualTo(user);
    }

    @Test
    void createConflict() {
        // Given
        User user = randomUser();
        this.sut.create(user);
        // When, Then
        assertThatThrownBy(() -> this.sut.create(user))
                .isInstanceOf(ConflictException.class);
    }

    int compareUsers(User u1, User u2) {
        return u1.getUsername().equals(u2.getUsername()) &&
                u1.getRole() == u2.getRole() ? 0 : -1;
    }

    User randomUser() {
        return new User(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Role.USER
        );
    }
}