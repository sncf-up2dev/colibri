package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Entity;

public record User(
    String username,
    String password,
    Role role
) implements Entity {

    @Override
    public String getId() {
        return this.username;
    }
}
