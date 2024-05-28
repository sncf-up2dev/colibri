package fr.sncf.d2d.colibri.conf;

import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.domain.users.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableConfigurationProperties(ColibriConfiguration.class)
public class AppUserService implements UserDetailsService {

    private final UserService userService;
    private final ColibriConfiguration configuration;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(
            ColibriConfiguration configuration,
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
            AppUser user = Optional.ofNullable(this.configuration.superuser())
                    .filter(u -> username.equals(u.username()))
                    .map(u -> new AppUser(u.username(), passwordEncoder.encode(u.password()), Role.ADMIN))
                    .orElseGet(() -> this.userService.retrieve(username));
            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole().getAuthorities())
            );
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("Username %s not found!".formatted(username), e);
        }
    }
}
