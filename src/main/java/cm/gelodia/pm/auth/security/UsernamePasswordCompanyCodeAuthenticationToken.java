package cm.gelodia.pm.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordCompanyCodeAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String companyCode;

    public UsernamePasswordCompanyCodeAuthenticationToken(Object principal, Object credentials, String companyCode) {
        super(principal, credentials);
        this.companyCode = companyCode;
        super.setAuthenticated(false);
    }

    public UsernamePasswordCompanyCodeAuthenticationToken(Object principal, Object credentials, String domain,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.companyCode = domain;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getCompanyCode() {
        return this.companyCode;
    }
}
