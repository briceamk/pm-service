package cm.gelodia.pm.auth.service;

import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {
    User create(UserPrincipal principal, User user);

    User update(UserPrincipal principal, User user);

    void addRoleToUser(UserPrincipal principal, String id, List<String> roles);

    Page<User> findAll(UserPrincipal principal, String firstName, String lastName, String username,
                       String email, String mobile, PageRequest pageRequest);

    User findById(UserPrincipal principal, String id);

    void deleteById(UserPrincipal principal, String id);

    long count();
}
