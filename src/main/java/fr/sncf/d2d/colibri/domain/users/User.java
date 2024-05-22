package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Model;

public record User(
    String username,
    String password,
    Role role
) implements Model {

    @Override
    public String getId() {
        return this.username;
    }
}
