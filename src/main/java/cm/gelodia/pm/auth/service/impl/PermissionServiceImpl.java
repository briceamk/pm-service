package cm.gelodia.pm.auth.service.impl;

import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.auth.model.PermissionCode;
import cm.gelodia.pm.auth.repository.PermissionRepository;
import cm.gelodia.pm.auth.service.PermissionService;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service("permissionService")
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission findById(String id) {
        return permissionRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Permission with Id {} not found",id);
                    throw new ResourceNotFoundException(String.format("Permission with Id %s not found", id));
                }
        );
    }

    @Override
    public Page<Permission> findAll(PermissionCode code, String name, PageRequest pageRequest) {
        Page<Permission> permissionPage;

        if (code != null) {
            //search by name
            permissionPage = permissionRepository.findByCode(code, pageRequest);
        }
        else if(!StringUtils.isEmpty(name)) {
            //search by name
            permissionPage = permissionRepository.findByNameContains(name, pageRequest);
        }
        else{
            // search all
            permissionPage = permissionRepository.findAll(pageRequest);
        }

        return permissionPage;
    }

    @Override
    public Permission findByCode(PermissionCode code) {
        return permissionRepository.findByCode(code).orElseThrow(
                () -> {
                    log.error("Permission with code {} not found",code);
                    throw new ResourceNotFoundException(String.format("Permission with code %s not found", code));
                }
        );
    }

    @Override
    public long count() {
        return permissionRepository.count();
    }
}
