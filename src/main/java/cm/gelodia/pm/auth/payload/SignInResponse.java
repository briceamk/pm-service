package cm.gelodia.pm.auth.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignInResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String city;
    private String mobile;
    private String accessToken;
    private String companyId;
    private String companyName;
    private String tokenType = "Bearer";
    private List<String> authorities;

    public SignInResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
