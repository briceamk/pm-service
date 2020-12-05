package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.CategoryDto;
import cm.gelodia.pm.catalog.model.Category;
import cm.gelodia.pm.company.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class CategoryMapperDecorator implements CategoryMapper{

    private CategoryMapper categoryMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public Category map(CategoryDto categoryDto) {
        Category category = categoryMapper.map(categoryDto);
        if(categoryDto.getCompanyDto() != null)
            category.setCompany(companyMapper.map(categoryDto.getCompanyDto()));
        if(categoryDto.getParentDto() != null)
            category.setParent(map(categoryDto.getParentDto()));
        return category;
    }

    @Override
    public CategoryDto map(Category category) {
        CategoryDto categoryDto = categoryMapper.map(category);
        if(category.getCompany() != null)
            categoryDto.setCompanyDto(companyMapper.map(category.getCompany()));
        if(category.getParent() != null)
            categoryDto.setParentDto(map(category.getParent()));
        return categoryDto;
    }
}
