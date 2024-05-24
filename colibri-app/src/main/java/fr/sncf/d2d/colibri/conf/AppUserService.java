package fr.sncf.d2d.colibri.conf;

import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.User;
import fr.sncf.d2d.colibri.domain.users.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.authority.AuthorityUtils;
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
                    .filter(u -> username.equals(u.getUsername()))
                    .map(u -> new User(u.getUsername(), passwordEncoder.encode(u.getPassword()), Role.ADMIN))
                    .orElseGet(() -> this.userService.retrieve(username));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole().getAuthorities())
            );
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("Username %s not found!".formatted(username), e);
        }
    }
}
