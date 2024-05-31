package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.Role;

public record UserPayload(
    String username,
    Role role
) {

    public static UserPayload from(AppUser user) {
        return new UserPayload(user.getUsername(), user.getRole());
    }
}
