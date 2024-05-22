package fr.sncf.d2d.colibri.domain.common;

public class ConflictException extends RuntimeException {

    public ConflictException() {
        this("Unique key conflict");
    }

    public ConflictException(String message) {
        super(message);
    }

}
