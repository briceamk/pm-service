package cm.gelodia.pm.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {
    @NotEmpty(message = "old password is required!")
    private String oldPassword;
    @NotEmpty(message = "new password is required")
    @Size(message = "minimal 6 characters is required!", min = 6)
    private String newPassword;
}
