package cm.gelodia.pm.auth.constant;

public class AuthConstantType {

    public static final String[] PUBLIC_MATCHERS = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/api/v1/companies/code/**"
    };

    public static final String BCRYPT_PATTERN = "\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}";
}
