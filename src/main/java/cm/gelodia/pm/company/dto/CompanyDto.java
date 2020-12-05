package cm.gelodia.pm.company.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto implements Serializable {

    static final long serialVersionUID = 8745353155335729757L;

    private String id;
    @NotEmpty(message = "code is required")
    @Size(max = 12, message= "code can have maximum 12 characters")
    private String code;
    @NotEmpty(message = "name is required")
    @Size(max = 64, message= "name can have maximum 64 characters")
    private String name;
    private String phoneNumber;
    private String mobileNumber;
    @Email(message = "Invalid email")
    private String email;
    private String city;
    private String zip;
    private String street;
    private String country;
    private String vat;
    private String trn;
    private String imageName;
    private Boolean active;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;
    private String imageType;

}
