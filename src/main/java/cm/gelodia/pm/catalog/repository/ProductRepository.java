package cm.gelodia.pm.catalog.repository;

import cm.gelodia.pm.catalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Boolean existsByName(String name);
    Boolean existsByReference(String reference);

    Page<Product> findByReferenceContainsIgnoreCase(String reference, Pageable pageable);

    Page<Product> findByNameContainsIgnoreCase(String name, Pageable pageable);

    Page<Product> findByDescriptionContainsIgnoreCase(String description, Pageable pageable);
}
