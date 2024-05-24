package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Model;

public class User implements Model {
    private final String username;
    private String password;
    private Role role;

    public User(
            String username,
            String password,
            Role role
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String getId() {
        return this.username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
