package fr.sncf.d2d.colibri.domain.common;

import java.util.List;

public record Page<T>(
        List<T> items,
        int size,
        int number,
        int totalPages
) {
    public Page(List<T> items, int number, int totalPages) {
        this(items, items.size(), number, totalPages);
    }
}
