package fr.sncf.d2d.colibri.domain.users;

import java.util.EnumSet;

public enum Role {
    USER,
    POSTMAN,
    ADMIN;

    public EnumSet<Role> getImpliedRoles() {
        return EnumSet.range(USER, this);
    }
}
