package cm.gelodia.pm.company.mapper;

import cm.gelodia.pm.company.dto.CompanyDto;
import cm.gelodia.pm.company.model.Company;
import org.mapstruct.Mapper;

@Mapper
public interface CompanyMapper {
    Company map(CompanyDto companyDto);
    CompanyDto map(Company company);
}
