package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.rest.common.Omissible;
import jakarta.validation.constraints.NotNull;

public final class UserModificationPayload {
    private Omissible<@Password String> password = Omissible.na();
    private Omissible<@NotNull Role> role = Omissible.na();

    public Omissible<@Password String> getPassword() {
        return password;
    }

    public Omissible<@NotNull Role> getRole() {
        return role;
    }

    public void setPassword(Omissible<@Password String> password) {
        this.password = password;
    }

    public void setRole(Omissible<@NotNull Role> role) {
        this.role = role;
    }
}
