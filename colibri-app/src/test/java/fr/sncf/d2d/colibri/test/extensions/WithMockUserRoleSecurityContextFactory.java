package fr.sncf.d2d.colibri.test.extensions;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserRoleSecurityContextFactory
        implements WithSecurityContextFactory<WithMockUserRole> {
    @Override
    public SecurityContext createSecurityContext(WithMockUserRole annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String[] auths = annotation.value().getAuthorities().toArray(String[]::new);
        Authentication auth = new TestingAuthenticationToken("test-principal", "", auths);
        context.setAuthentication(auth);
        return context;
    }
}
