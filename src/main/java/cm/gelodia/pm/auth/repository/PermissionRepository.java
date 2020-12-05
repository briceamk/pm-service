package cm.gelodia.pm.auth.repository;

import cm.gelodia.pm.auth.model.PermissionCode;
import cm.gelodia.pm.auth.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByCode(PermissionCode PermissionCode);

    Page<Permission> findByCode(PermissionCode code, Pageable pageable);


    Page<Permission> findByNameContains(String name, Pageable pageable);
}
