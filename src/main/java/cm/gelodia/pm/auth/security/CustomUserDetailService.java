package cm.gelodia.pm.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public  interface CustomUserDetailService {

    UserDetails loadUserByUsernameAndCompanyCode(String usernameOrEmail, String companyCode);
    UserDetails loadUserByUsername(String usernameOrEmail);
    UserDetails loadUserById(String id);
}
