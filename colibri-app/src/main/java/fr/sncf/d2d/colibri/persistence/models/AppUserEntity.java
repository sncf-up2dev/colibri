package fr.sncf.d2d.colibri.persistence.models;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity(name = "AppUser")
public final class AppUserEntity {
    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
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

    public AppUser toUser() {
        return new AppUser(this.username, this.password, this.role);
    }

    public static AppUserEntity from(AppUser user) {
        AppUserEntity entity = new AppUserEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return entity;
    }

    public static AppUserEntity fromUsername(String username) {
        AppUserEntity user = new AppUserEntity();
        user.setUsername(username);
        return user;
    }
}
