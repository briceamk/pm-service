package cm.gelodia.pm.notification.dto;


import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailServerDto implements Serializable {

    static final long serialVersionUID = -7254403732415306016L;

    private String id;
    @NotEmpty(message = "hostname is required!")
    private String hostname;
    @NotEmpty(message = "port is required!")
    @Pattern(regexp = "\\d{2,5}", message = "port must have 5 numeric value")
    private String port;
    @Email(message = "invalid email!")
    private String username;
    private String password;
    private Boolean enableSSL;
    private Boolean enableAuth;
    @NotEmpty(message = "type is required")
    private String type;
    private String state;
    private Boolean defaultServer;
    private CompanyDto companyDto;

}
