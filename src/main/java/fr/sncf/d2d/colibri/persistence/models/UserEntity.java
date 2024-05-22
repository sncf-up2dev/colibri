package fr.sncf.d2d.colibri.persistence.models;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public final class UserEntity {
    @Id
    private String username;
    private String password;
    private Role role;

    public String getId() {
        return this.username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User toUser() {
        return new User(this.username, this.password, this.role);
    }

    public static UserEntity from(User user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.username());
        entity.setPassword(user.password());
        entity.setRole(user.role());
        return entity;
    }
}
