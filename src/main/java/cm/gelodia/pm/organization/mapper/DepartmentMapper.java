package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.organization.dto.DepartmentDto;
import cm.gelodia.pm.organization.model.Department;
import org.mapstruct.Mapper;

@Mapper
public interface DepartmentMapper {
     Department map(DepartmentDto departmentDto);

    DepartmentDto map(Department department);
}
