package fr.sncf.d2d.colibri.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "colibri")
public record ColibriConfiguration(
        Superuser superuser
) {

    public record Superuser(String username, String password) {}
}
