package fr.sncf.d2d.colibri.persistence.errors;

import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionInterceptor {

    @AfterThrowing(pointcut = "@annotation(ignore) || @within(ignore)", throwing = "ex")
    public Object handle(MapExceptions ignore, Throwable ex) {
        throw this.map(ex);
    }

    public RuntimeException map(Throwable ex) {
        return switch (ex) {
            case EntityExistsException e -> new ConflictException("Entity already exists", e);
            case EntityNotFoundException e -> new NotFoundException("Entity not found", e);
            case RuntimeException e -> e.getCause() == null ? e : map(ex.getCause());
            default -> ex.getCause() == null ? new IllegalOperationException(ex) : map(ex.getCause());
        };
    }
}
