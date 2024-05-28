package fr.sncf.d2d.colibri.rest.users;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.AppUser;

public record UserModificationPayload(
        String username,
        String password,
        Role role
) {

    public AppUser toAppUser() {
        return new AppUser(username, password, role);
    }
}
