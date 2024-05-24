package fr.sncf.d2d.colibri.domain.common;

import fr.sncf.d2d.colibri.domain.users.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }
}
