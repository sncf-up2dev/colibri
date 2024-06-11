package fr.sncf.d2d.colibri.graphql;

import fr.sncf.d2d.colibri.domain.common.ConflictException;
import fr.sncf.d2d.colibri.domain.common.IllegalOperationException;
import fr.sncf.d2d.colibri.domain.common.NotFoundException;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class HttpErrorHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    public GraphQLError resolveToSingleError(
            Throwable exception,
            DataFetchingEnvironment environment) {
        ErrorClassification errorType = switch (exception) {
            case ConflictException  ignore -> ErrorType.CONFLICT;
            case IllegalOperationException ignore -> ErrorType.BAD_REQUEST;
            case NotFoundException ignore -> ErrorType.NOT_FOUND;
            default -> ErrorType.INTERNAL_ERROR;
        };
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(exception.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }

    public enum ErrorType implements ErrorClassification {
        BAD_REQUEST,
        CONFLICT,
        NOT_FOUND,
        INTERNAL_ERROR,
    }
}
