package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.CategoryDto;
import cm.gelodia.pm.catalog.model.Category;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(CategoryMapperDecorator.class)
public interface CategoryMapper {
    Category map(CategoryDto categoryDto);
    CategoryDto map(Category category);
}
