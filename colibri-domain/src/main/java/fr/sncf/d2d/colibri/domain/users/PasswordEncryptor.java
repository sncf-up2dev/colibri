package fr.sncf.d2d.colibri.domain.users;

public interface PasswordEncryptor {

    String encode(String password);
}
