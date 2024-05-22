package fr.sncf.d2d.colibri.conf;

import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.User;
import fr.sncf.d2d.colibri.domain.users.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableConfigurationProperties(AppConfiguration.class)
public class AppUserService implements UserDetailsService {

    private final UserService userService;
    private final AppConfiguration configuration;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(
            AppConfiguration configuration,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.configuration = configuration;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = Optional.ofNullable(this.configuration.superuser())
                    .filter(u -> username.equals(u.username()))
                    .map(u -> new User(u.username(), passwordEncoder.encode(u.password()), Role.ADMIN))
                    .orElseGet(() -> this.userService.retrieve(username));
            return new org.springframework.security.core.userdetails.User(
                    user.username(),
                    user.password(),
                    user.role().getImpliedRoles().stream().map(Enum::name).map(SimpleGrantedAuthority::new).toList()
            );
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("Username %s not found!".formatted(username), e);
        }
    }
}
