package fr.sncf.d2d.colibri.conf;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppPasswordEncoder
        implements fr.sncf.d2d.colibri.domain.users.PasswordEncoder {

    private final PasswordEncoder encoder;

    public AppPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String password) {
        return this.encoder.encode(password);
    }
}
