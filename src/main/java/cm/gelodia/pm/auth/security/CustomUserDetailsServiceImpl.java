package cm.gelodia.pm.auth.security;

import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public  class CustomUserDetailsServiceImpl implements CustomUserDetailService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
            // Let people login with either username or email
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                    );
            UserPrincipal userPrincipal = UserPrincipal.create(user);
            new AccountStatusUserDetailsChecker().check(userPrincipal);
            return userPrincipal;
        }

    @Override
    @Transactional
    public UserDetails loadUserByUsernameAndCompanyCode(String usernameOrEmail, String companyCode) throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmailAndCompanyCode(usernameOrEmail, usernameOrEmail, companyCode)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User not found with username or email %s and company code %s", usernameOrEmail, companyCode))
                );
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        new AccountStatusUserDetailsChecker().check(userPrincipal);
        return userPrincipal;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        new AccountStatusUserDetailsChecker().check(userPrincipal);
        return userPrincipal;
    }
}
