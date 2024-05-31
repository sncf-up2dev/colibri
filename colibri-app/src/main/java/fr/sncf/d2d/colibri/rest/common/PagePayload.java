package fr.sncf.d2d.colibri.rest.common;

import fr.sncf.d2d.colibri.domain.common.Page;

import java.util.List;
import java.util.function.Function;

public record PagePayload<T>(
        List<T> result,
        int page,
        int size,
        int totalPages
) {

    public static <I, T> PagePayload<T> from(Page<I> page, Function<I, T> mapper) {
        return new PagePayload<>(
                page.items().stream().map(mapper).toList(),
                page.number(),
                page.size(),
                page.totalPages()
        );
    }
}
