package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Base64PasswordEncryptor;
import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.domain.common.Page;
import fr.sncf.d2d.colibri.domain.common.PageSpecs;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppUserServiceTest {

    UserRepository repository = new InMemUserRepository();
    UserService sut = new UserService(
            Logger.getAnonymousLogger(),
            repository,
            new Base64PasswordEncryptor()
    );

    @Test
    void retrieveOne() {
        // Given
        AppUser user = randomUser();
        this.sut.create(user);
        // When
        AppUser retrieved = this.sut.retrieve(user.getUsername());
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
        List<AppUser> user = Stream.generate(this::randomUser).limit(10).toList();
        user.forEach(this.sut::create);
        // When
        List<AppUser> retrieved = this.sut.retrieve();
        // Then
        assertThat(retrieved)
                .usingComparatorForType(this::compareUsers, AppUser.class)
                .isSubsetOf(user);
    }

    @Test
    void testRetrieveFirstPage() {
        // Given
        List<AppUser> user = Stream.generate(this::randomUser).limit(10).toList();
        user.forEach(this.sut::create);
        // When
        Page<AppUser> page = this.sut.retrieve(new PageSpecs(0, 4));
        // Then
        assertThat(page.number()).isEqualTo(0);
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.totalPages()).isGreaterThanOrEqualTo(3);
        assertThat(page.items())
                .usingComparatorForType(this::compareUsers, AppUser.class)
                .isSubsetOf(user);
    }

    @Test
    void testRetrieveSecondPage() {
        // Given
        List<AppUser> user = Stream.generate(this::randomUser).limit(10).toList();
        user.forEach(this.sut::create);
        // When
        Page<AppUser> page = this.sut.retrieve(new PageSpecs(1, 4));
        // Then
        assertThat(page.number()).isEqualTo(1);
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.totalPages()).isGreaterThanOrEqualTo(3);
        assertThat(page.items())
                .usingComparatorForType(this::compareUsers, AppUser.class)
                .isSubsetOf(user);
    }

    @Test
    void delete() {
        // Given
        AppUser user = randomUser();
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
        AppUser user = randomUser();
        this.sut.create(user);
        // When
        this.sut.update(user.getUsername(), u -> u.setRole(Role.POSTMAN));
        // Then
        AppUser retrieved = this.sut.retrieve(user.getUsername());
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
        AppUser user = randomUser();
        // When
        AppUser created = this.sut.create(user);
        // Then
        assertThat(created)
                .usingComparator(this::compareUsers)
                .isEqualTo(user);
        AppUser retrieved = this.sut.retrieve(user.getUsername());
        assertThat(retrieved)
                .usingComparator(this::compareUsers)
                .isEqualTo(user);
    }

    @Test
    void createConflict() {
        // Given
        AppUser user = randomUser();
        this.sut.create(user);
        // When, Then
        assertThatThrownBy(() -> this.sut.create(user))
                .isInstanceOf(ConflictException.class);
    }

    int compareUsers(AppUser u1, AppUser u2) {
        return u1.getUsername().equals(u2.getUsername()) &&
                u1.getRole() == u2.getRole() ? 0 : -1;
    }

    AppUser randomUser() {
        return new AppUser(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Role.USER
        );
    }
}