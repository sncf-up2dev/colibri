package fr.sncf.d2d.colibri.rest.common;

import fr.sncf.d2d.colibri.domain.common.PageSpecs;

public record PageParams(
        Integer page,
        Integer size
) {
    public static int DEFAULT_SIZE = 50;


    public PageSpecs toPageSpecs() {
        return new PageSpecs(
                null == page ? 0 : this.page,
                null == size ? DEFAULT_SIZE : this.size
        );
    }
}
