package cm.gelodia.pm.organization.repository;

import cm.gelodia.pm.organization.model.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, String> {
    Boolean existsByName(String name);

    Boolean existsByCode(String code);

    Page<Charge> findByCodeContainsIgnoreCase(String code, Pageable pageable);

    Page<Charge> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
