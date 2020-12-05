package cm.gelodia.pm.catalog.mapper;

import cm.gelodia.pm.catalog.dto.ProductDto;
import cm.gelodia.pm.catalog.model.Product;
import cm.gelodia.pm.company.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProductMapperDecorator implements ProductMapper{

    private ProductMapper productMapper;
    private CompanyMapper companyMapper;
    private CatalogMapper catalogMapper;
    private CategoryMapper categoryMapper;

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Autowired
    public void setCatalogMapper(CatalogMapper catalogMapper) {
        this.catalogMapper = catalogMapper;
    }

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Product map(ProductDto productDto) {
        Product product = productMapper.map(productDto);

        if(productDto.getCompanyDto() != null)
            product.setCompany(companyMapper.map(productDto.getCompanyDto()));
        if(productDto.getCategoryDto() != null)
            product.setCategory(categoryMapper.map(productDto.getCategoryDto()));

        if(productDto.getCatalogDto() != null)
            product.setCatalog(catalogMapper.map(productDto.getCatalogDto()));
        return product;
    }

    @Override
    public ProductDto map(Product product) {
        ProductDto productDto = productMapper.map(product);

        if(product.getCategory() != null)
            productDto.setCategoryDto(categoryMapper.map(product.getCategory()));

        if(product.getCatalog() != null)
            productDto.setCatalogDto(catalogMapper.map(product.getCatalog()));
        if(product.getCompany() != null)
            productDto.setCompanyDto(companyMapper.map(product.getCompany()));
        return productDto;
    }
}
