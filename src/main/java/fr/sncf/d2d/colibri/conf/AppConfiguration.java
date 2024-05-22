package fr.sncf.d2d.colibri.conf;

import fr.sncf.d2d.colibri.domain.users.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "colibri")
public record AppConfiguration(
        User superuser
) {

    public record Superuser(String username, String password) {}
}
