package cm.gelodia.pm.auth.dto;


import cm.gelodia.pm.company.dto.CompanyDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    static final long serialVersionUID = -2404307862580487923L;

    private String id;
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Username is required")
    private String username;
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    private String city;
    private String mobile;
    @NotEmpty(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull(message = "company is required")
    private CompanyDto companyDto;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private Set<PermissionDto> permissionDtos;
}
