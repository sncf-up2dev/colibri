package fr.sncf.d2d.colibri.domain.users;

import java.util.EnumSet;
import java.util.List;

public enum Role {
    USER,
    POSTMAN,
    ADMIN;

    public EnumSet<Role> getImpliedRoles() {
        return EnumSet.range(USER, this);
    }

    public List<String> getAuthorities() {
        return this.getImpliedRoles().stream().map(Enum::name).toList();
    }
}
