package cm.gelodia.pm.auth.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignUpRequest {
    @Size(min = 4, max = 128)
    private String firstName;

    @NotEmpty(message = "lastName name is required!")
    @Size(min = 4, max = 128)
    private String lastName;

    @NotEmpty(message = "username is required!")
    @Size(min = 5, max = 32, message = "username size must between 5 and 32 characters!")
    private String username;

    @NotEmpty(message = "email is required!")
    @Size(max = 128)
    @Email(message = "invalid email!")
    private String email;

    @NotEmpty(message = "company code is required!")
    @Size(min = 5, max=5, message = "company code size is exactly 5 characters")
    private String companyCode;


    @NotEmpty(message = "password is required!")
    @Size(min = 6,  message = "password size must between and 64 characters!")
    private String password;
}
