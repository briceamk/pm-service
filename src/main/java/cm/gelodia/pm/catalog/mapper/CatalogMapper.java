package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.CatalogDto;
import cm.gelodia.pm.catalog.model.Catalog;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(CatalogMapperDecorator.class)
public interface CatalogMapper {

    Catalog map(CatalogDto catalogDto);
    CatalogDto map(Catalog catalog);
}
