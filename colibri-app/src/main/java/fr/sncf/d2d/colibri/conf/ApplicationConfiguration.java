package fr.sncf.d2d.colibri.conf;

import fr.sncf.d2d.colibri.domain.users.PasswordEncryptor;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint injectionPoint) {
        String name = injectionPoint.getMember().getDeclaringClass().getName();
        return Logger.getLogger(name);
    }

    @Bean
    public PasswordEncryptor passwordEncryptor(PasswordEncoder encoder) {
        return encoder::encode;
    }
}
