package cm.gelodia.pm.catalog.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {

    static final long serialVersionUID = -8860696830742143712L;

    private String id;
    @NotEmpty(message = "name is required!")
    private String name;
    private String description;
    private Boolean active;
    private CategoryDto parentDto;
    private CompanyDto companyDto;
}
