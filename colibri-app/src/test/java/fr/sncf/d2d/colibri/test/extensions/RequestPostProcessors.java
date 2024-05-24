package fr.sncf.d2d.colibri.test.extensions;

import fr.sncf.d2d.colibri.domain.users.Role;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public interface RequestPostProcessors {

    static RequestPostProcessor userRole(Role role) {
        return user(UUID.randomUUID().toString())
                .authorities(AuthorityUtils.createAuthorityList(role.getAuthorities()));
    }
}
