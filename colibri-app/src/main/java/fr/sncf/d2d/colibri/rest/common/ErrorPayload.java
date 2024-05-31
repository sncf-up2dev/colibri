package fr.sncf.d2d.colibri.rest.common;

public record ErrorPayload(
    String[] errors
) {
    public ErrorPayload(String error) {
        this(new String[] { error });
    }

    public static ErrorPayload of(Exception exception) {
        return new ErrorPayload(exception.getMessage());
    }
}
