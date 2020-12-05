package cm.gelodia.pm.auth.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {

    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }
}
