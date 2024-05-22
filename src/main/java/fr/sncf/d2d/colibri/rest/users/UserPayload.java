package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.User;

import java.util.Collection;
import java.util.List;

record UserPayload(
    String username,
    String password,
    Role role
) {

    public static UserPayload from(User user) {
        return new UserPayload(user.username(), user.password(), user.role());
    }

    public static List<UserPayload> from(Collection<User> users) {
        return users.stream().map(UserPayload::from).toList();
    }

    public User toUser() {
        return new User(username, password, role);
    }
}
