package cm.gelodia.pm.catalog.repository;


import cm.gelodia.pm.catalog.model.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, String> {
    Boolean existsByName(String name);

    Page<Catalog> findByNameContainsIgnoreCase(String name, Pageable pageable);

    Page<Catalog> findByDescriptionContainsIgnoreCase(String description, Pageable pageable);
}
