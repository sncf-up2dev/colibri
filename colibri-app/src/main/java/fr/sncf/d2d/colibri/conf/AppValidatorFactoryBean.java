package fr.sncf.d2d.colibri.conf;

import jakarta.validation.Configuration;
import jakarta.validation.valueextraction.ValueExtractor;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

@Component
public class AppValidatorFactoryBean extends LocalValidatorFactoryBean {

    private final List<ValueExtractor<?>> valueExtractors;

    public AppValidatorFactoryBean(
            List<ValueExtractor<?>> valueExtractors
    ) {
        this.valueExtractors = valueExtractors;
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> configuration) {
        super.postProcessConfiguration(configuration);

        this.valueExtractors.forEach(configuration::addValueExtractor);
    }
}
