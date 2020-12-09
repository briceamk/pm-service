package cm.gelodia.pm.auth.payload;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String city;
    private String mobile;
    private String accessToken;
    private String refreshToken;
    private String companyId;
    private String companyName;
    private String tokenType = "Bearer";
    private List<String> authorities;

}
