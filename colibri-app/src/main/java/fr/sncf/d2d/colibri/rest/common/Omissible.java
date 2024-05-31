package fr.sncf.d2d.colibri.rest.common;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.function.Consumer;

public sealed interface Omissible<T>
    permits Omissible.Available, Omissible.NotAvailable {

    @SuppressWarnings("unchecked")
    static <T> Omissible<T> na() {
        return (NotAvailable<T>) NotAvailable.INSTANCE;
    }

    @JsonCreator
    static <T> Available<T> of(T value) {
        return new Available<>(value);
    }

    void ifAvailable(Consumer<T> consumer);

    record Available<T>(T value) implements Omissible<T> {

        @Override
        public void ifAvailable(Consumer<T> consumer) {
            consumer.accept(this.value);
        }
    }

    final class NotAvailable<T> implements Omissible<T> {
        private static final NotAvailable<?> INSTANCE = new NotAvailable<>();

        private NotAvailable() {}

        @Override
        public void ifAvailable(Consumer<T> consumer) {
            // Do nothing
        }
    }
}
