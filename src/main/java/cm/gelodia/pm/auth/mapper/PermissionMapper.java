package cm.gelodia.pm.auth.mapper;

import cm.gelodia.pm.auth.dto.PermissionDto;
import cm.gelodia.pm.auth.model.Permission;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface PermissionMapper {
    Permission map(PermissionDto PermissionDto);
    PermissionDto map(Permission permission);
    Set<Permission> mapToListEntity(Set<PermissionDto> permissionDtos);
    Set<PermissionDto> mapToListDto(Set<Permission> permissions);
}
