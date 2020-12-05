package cm.gelodia.pm.catalog.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDto implements Serializable {

    static final long serialVersionUID = -8830511432217114144L;

    private String id;
    @NotEmpty(message = "name is required")
    private String name;
    private String description;
    private Boolean active;
    private CatalogDto parentDto;
    private CompanyDto companyDto;
}
