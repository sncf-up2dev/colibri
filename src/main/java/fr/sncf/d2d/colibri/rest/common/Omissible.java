package fr.sncf.d2d.colibri.rest.common;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;
import java.util.function.Consumer;

public sealed interface Omissible<T>
    permits Omissible.Available, Omissible.NotAvailable {

    @SuppressWarnings("unchecked")
    static <T> Omissible<T> na() {
        return (NotAvailable<T>) NotAvailable.INSTANCE;
    }

    @JsonCreator
    static <T> Omissible<T> of(T value) {
        return value == null ? na() : Available.of(value);
    }

    default void ifPresent(Consumer<T> consumer) {
        if (this instanceof Omissible.Available<T> available) {
            consumer.accept(available.value);
        }
    }

    final class Available<T> implements Omissible<T> {
        private final T value;

        private Available(T value) {
            this.value = value;
        }

        public static <T> Available<T> of(T value) {
            Objects.requireNonNull(value);
            return new Available<>(value);
        }
    }

    final class NotAvailable<T> implements Omissible<T> {
        private static final NotAvailable<?> INSTANCE = new NotAvailable<>();

        private NotAvailable() {}
    }
}
