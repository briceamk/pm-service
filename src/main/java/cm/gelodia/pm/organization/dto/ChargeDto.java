package cm.gelodia.pm.organization.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDto {
    static final long serialVersionUID = 7919035040372241812L;

    private String id;
    @NotEmpty(message = "code is required")
    private String code;
    @NotEmpty(message = "name is required")
    private String name;
    private Boolean isInternational;
    private Boolean active;
    private CompanyDto companyDto;
}
