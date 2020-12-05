package cm.gelodia.pm.catalog.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable {

    static final long serialVersionUID = -1871664834413126890L;

    private String id;
    @NotEmpty(message = "reference is required")
    private String reference;
    @NotEmpty(message = "name is required")
    private String name;
    private String description;
    private BigDecimal standardCostPrice;
    private String imageType;
    private String imageName;
    private Boolean active;
    @NotNull(message = "category is required")
    private CategoryDto categoryDto;
    @NotNull(message = "catalog is required")
    private CatalogDto catalogDto;
    private CompanyDto companyDto;
}
