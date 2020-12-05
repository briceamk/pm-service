package cm.gelodia.pm.auth.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignInRequest {
    @NotEmpty(message = "username or email is required")
    @Size(min = 5, message = "username or email should have minimum 5 characters")
    private String usernameOrEmail;

    @NotEmpty(message = "company code is required")
    @Size(min = 5, max=5, message = "company code size is exactly 5 characters")
    private String companyCode;

    @NotEmpty(message = "password is required")
    @Size(min = 6, message = "password should have minimum 5 characters")
    private String password;
}
