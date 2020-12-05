package cm.gelodia.pm.catalog.repository;


import cm.gelodia.pm.catalog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByName(String name);

    Page<Category> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
