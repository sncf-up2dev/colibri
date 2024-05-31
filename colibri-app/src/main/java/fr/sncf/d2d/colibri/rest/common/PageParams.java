package fr.sncf.d2d.colibri.rest.common;

import fr.sncf.d2d.colibri.domain.common.PageSpecs;

public record PageParams(
        Integer page,
        Integer size
) {
    public static int DEFAULT_SIZE = 50;


    public PageSpecs toPageSpecs() {
        return new PageSpecs(
                page == null ? 0 : page,
                size == null ? DEFAULT_SIZE : size
        );
    }
}
