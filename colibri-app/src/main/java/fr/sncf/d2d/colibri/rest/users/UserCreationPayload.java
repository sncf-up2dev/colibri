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

    @AssertTrue(message = "Password cannot contain username.")
    public boolean isValidPassword() {
        return !this.password.contains(this.username);
    }

    public AppUser toAppUser() {
        return new AppUser(this.username, this.password, this.role);
    }
}
