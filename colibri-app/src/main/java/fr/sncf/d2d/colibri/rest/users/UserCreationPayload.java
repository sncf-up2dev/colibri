package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.Role;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record UserCreationPayload(
        @Username
        String username,
        @Password String password,
        @NotNull Role role
) {

    @AssertTrue
    public boolean isValid() {
        return !password.contains(username);
    }

    public AppUser toAppUser() {
        return new AppUser(username, password, role);
    }
}
