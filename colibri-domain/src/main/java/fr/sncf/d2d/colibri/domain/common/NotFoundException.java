package fr.sncf.d2d.colibri.domain.common;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        this("Element not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
