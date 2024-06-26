package fr.sncf.d2d.colibri.domain.users;

import fr.sncf.d2d.colibri.domain.common.Model;

public class AppUser implements Model {
    private final String username;
    private String password;
    private Role role;

    public AppUser(
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
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
