package fr.sncf.d2d.colibri.rest.common;

public record ErrorPayload(
    String message
) {

    public static ErrorPayload of(Exception exception) {
        return new ErrorPayload(exception.getMessage());
    }
}
