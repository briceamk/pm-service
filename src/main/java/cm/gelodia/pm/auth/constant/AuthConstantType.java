package cm.gelodia.pm.auth.constant;

public class AuthConstantType {

    public static final String[] PUBLIC_MATCHERS = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/api/v1/companies/code/**"
    };
}
