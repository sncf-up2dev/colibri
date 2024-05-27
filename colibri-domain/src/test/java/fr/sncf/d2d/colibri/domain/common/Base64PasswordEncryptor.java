package fr.sncf.d2d.colibri.domain.common;

import fr.sncf.d2d.colibri.domain.users.PasswordEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64PasswordEncryptor implements PasswordEncryptor {
    @Override
    public String encode(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }
}
