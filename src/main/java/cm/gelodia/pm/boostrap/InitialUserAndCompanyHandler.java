package cm.gelodia.pm.boostrap;

import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.auth.model.PermissionCode;
import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.auth.service.PermissionService;
import cm.gelodia.pm.auth.service.UserService;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialUserAndCompanyHandler implements ApplicationRunner {

    private final CompanyService companyService;
    private final UserService userService;
    private final PermissionService permissionService;
    @Value("${account.admin.first-name}")
    private String firstName;
    @Value("${account.admin.last-name}")
    private String lastName;
    @Value("${account.admin.username}")
    private String username;
    @Value("${account.admin.email}")
    private String email;
    @Value("${account.admin.password}")
    private String password;
    @Value("${account.admin.roles}")
    private String rules;
    @Value("${company.name}")
    private String companyName;
    @Value("${company.code}")
    private String companyCode;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>>>>>>>>>>>> Start loading initial Data for Company, Role and User");
        Set<Permission> permissions = new LinkedHashSet<>();
        if(permissionService.count() == 0)  {
            Arrays.stream(rules.split(",")).forEach(rule ->
                    permissions.add(permissionService.create(
                            Permission.builder().code(PermissionCode.valueOf(rule)).name(StringUtils.capitalize(rule.replace("ROLE_", "").toLowerCase())).build())
                    ));
        }

        if(userService.count() == 0) {
            User user = userService.create(null,
                    User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .username(username)
                            .email(email)
                            .password(password)
                            .permissions(permissions)
                            .build()
            );

            Company company =Company.builder().code(companyCode).name(companyName).active(true).build();
            company.setCreatedBy(user.getId());
            company = companyService.create(UserPrincipal.create(user), company);

            user.setCompany(company);
            userService.update(UserPrincipal.create(user), user);
        }



        log.info(">>>>>>>>>>>>>>>>> Stop loading initial Data for Company, Permission and User");
    }
}
