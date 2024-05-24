package fr.sncf.d2d.colibri.domain.users;

public interface PasswordEncoder {

    String encode(String password);
}
