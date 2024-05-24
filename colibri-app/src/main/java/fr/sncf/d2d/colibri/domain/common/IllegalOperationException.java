package fr.sncf.d2d.colibri.domain.common;

public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException() {
        this("Illegal operation");
    }


    public IllegalOperationException(String message) {
        super(message);
    }
}
