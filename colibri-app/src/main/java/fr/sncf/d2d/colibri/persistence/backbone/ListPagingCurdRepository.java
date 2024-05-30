package fr.sncf.d2d.colibri.persistence.backbone;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ListPagingCurdRepository<T>
        extends ListCrudRepository<T, String>, ListPagingAndSortingRepository<T, String> {
}
