package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.ProductDto;
import cm.gelodia.pm.catalog.model.Product;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {

    Product map(ProductDto productDto);
    ProductDto map(Product product);
}
