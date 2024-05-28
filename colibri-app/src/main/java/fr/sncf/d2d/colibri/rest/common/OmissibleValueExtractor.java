package fr.sncf.d2d.colibri.rest.common;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;
import org.springframework.stereotype.Component;

@Component
public class OmissibleValueExtractor implements ValueExtractor<Omissible<@ExtractedValue ?>> {
    @Override
    public void extractValues(Omissible<?> originalValue, ValueReceiver receiver) {
        originalValue.ifAvailable(value -> receiver.value(null, value));
    }
}
