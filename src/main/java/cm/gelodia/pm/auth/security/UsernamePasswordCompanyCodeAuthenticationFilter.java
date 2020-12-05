package cm.gelodia.pm.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UsernamePasswordCompanyCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_COMPANY_CODE_KEY = "companyCode";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordCompanyCodeAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordCompanyCodeAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String companyCode = obtainCompanyCode(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (companyCode == null) {
            companyCode = "";
        }

        return new UsernamePasswordCompanyCodeAuthenticationToken(username, password, companyCode);
    }

    private String obtainCompanyCode(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_COMPANY_CODE_KEY);
    }
}