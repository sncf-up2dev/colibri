package fr.sncf.d2d.colibri.test.extensions;

import fr.sncf.d2d.colibri.domain.users.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserRoleSecurityContextFactory.class)
public @interface WithMockUserRole {

    Role value() default Role.USER;
}
