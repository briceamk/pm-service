package cm.gelodia.pm.organization.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.organization.model.AddressType;
import cm.gelodia.pm.organization.model.Department;
import cm.gelodia.pm.organization.model.Partner;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    static final long serialVersionUID = 2966454967571350491L;

    private String id;
   @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "type is required")
    private AddressType type;
    private String vat;
    private String trn;
    private String title;
    private String firstName;
    private String lastName;
    private String street;
    private String zip;
    @Email(message = "Invalid email")
    private String email;
    private String phone;
    private String mobile;
    private String fax;
    private String website;
    private String city;
    private String country;
    private String imageHeaderName;
    private String imageHeaderType;
    private String imageFooterName;
    private String imageFooterType;
    private CompanyDto companyDto;
    private DepartmentDto departmentDto;
    private PartnerDto partnerDto;

}
