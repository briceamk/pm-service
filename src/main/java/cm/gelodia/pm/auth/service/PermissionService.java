package cm.gelodia.pm.auth.service;

import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.auth.model.PermissionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PermissionService {
    Permission create(Permission permission);
    Permission update(Permission permission);
    Permission findById(String id);
    Page<Permission> findAll(PermissionCode code, String name, PageRequest pageRequest);
    Permission findByCode(PermissionCode code);
    long count();
}
