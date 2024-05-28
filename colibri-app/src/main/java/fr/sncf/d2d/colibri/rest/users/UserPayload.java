package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.AppUser;

import java.util.Collection;
import java.util.List;

public record UserPayload(
    String username,
    Role role
) {

    public static UserPayload from(AppUser user) {
        return new UserPayload(user.getUsername(), user.getRole());
    }

    public static List<UserPayload> from(Collection<AppUser> users) {
        return users.stream().map(UserPayload::from).toList();
    }
}
