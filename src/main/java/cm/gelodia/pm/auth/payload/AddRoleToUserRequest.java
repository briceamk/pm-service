package cm.gelodia.pm.auth.payload;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleToUserRequest implements Serializable {

    @NotEmpty(message = "user id is required")
    private String userId;
    @NotNull(message = "roles is required")
    private List<String> roles;
}
