package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.CatalogDto;
import cm.gelodia.pm.catalog.model.Catalog;
import cm.gelodia.pm.company.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CatalogMapperDecorator implements CatalogMapper {

    private CatalogMapper catalogMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public void setCatalogMapper(CatalogMapper catalogMapper) {
        this.catalogMapper = catalogMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public Catalog map(CatalogDto catalogDto) {
        Catalog catalog = catalogMapper.map(catalogDto);
        if(catalogDto.getCompanyDto() != null)
            catalog.setCompany(companyMapper.map(catalogDto.getCompanyDto()));
        if(catalogDto.getParentDto() != null)
            catalog.setParent(map(catalogDto.getParentDto()));
        return catalog;
    }

    @Override
    public CatalogDto map(Catalog catalog) {
        CatalogDto catalogDto = catalogMapper.map(catalog);
        if(catalog.getCompany() != null)
            catalogDto.setCompanyDto(companyMapper.map(catalog.getCompany()));
        if(catalog.getParent() != null)
            catalogDto.setParentDto(map(catalog.getParent()));
        return catalogDto;
    }
}
