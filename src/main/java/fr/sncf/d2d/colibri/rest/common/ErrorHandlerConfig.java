package fr.sncf.d2d.colibri.rest.common;

import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerConfig {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorPayload handle(ConflictException ex) {
        return ErrorPayload.of(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorPayload handle(NotFoundException ex) {
        return ErrorPayload.of(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayload handle(IllegalOperationException ex) {
        return ErrorPayload.of(ex);
    }
}
