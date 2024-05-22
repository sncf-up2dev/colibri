package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.User;

public record UserModificationPayload(
        String username,
        String password,
        Role role
) {

    public User toUser() {
        return new User(username, password, role);
    }
}
