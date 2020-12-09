package cm.gelodia.pm.auth.service.impl;

import cm.gelodia.pm.auth.constant.AuthConstantType;
import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.auth.model.PermissionCode;
import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.repository.UserRepository;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.auth.service.PermissionService;
import cm.gelodia.pm.auth.service.UserService;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;
    private final Pattern bcryptPattern = Pattern.compile(AuthConstantType.BCRYPT_PATTERN);

    @Override
    @Transactional
    public User create(UserPrincipal principal, User user) {
        // we check if username or email mobile is already used
        // for default user, we don't need to check this
        if (user.getCompany() != null && userRepository.checkByUsernameAndCompanyCode(user.getUsername(), user.getCompany().getCode())) {
            log.error("Username {} already used for company {}.", user.getUsername(), user.getCompany().getName());
            throw new ConflictException(String.format("Username %s already used for company %s.", user.getUsername(),  user.getCompany().getName()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("Email {} already used.", user.getEmail());
            throw new ConflictException(String.format("Email %s already used.", user.getEmail()));
        }

        if (user.getMobile() != null && userRepository.existsByMobile(user.getMobile())) {
            log.error("Mobile {} already used.", user.getMobile());
            throw new ConflictException(String.format("Mobile %s already used.", user.getMobile()));
        }
        // this will be use in case we need to send mail to user, so he ca, enable his account
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        if( user.getPermissions().stream().anyMatch(permission -> PermissionCode.ROLE_MANAGER.equals(permission.getCode()) || PermissionCode.ROLE_ADMIN.equals(permission.getCode())))
            user.setEnabled(true);
        else
            user.setEnabled(false);
        //TODO save token verification and send Email Activation

        return userRepository.save(user);
    }

    @Override
    public User update(UserPrincipal principal, User user) {
        User dbUser = findById(principal, user.getId());
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(dbUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user.getCompany() == null) {
            user.setCompany(dbUser.getCompany());
        }
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(UserPrincipal principal, String id, List<String> roles) {
        User user = findById(principal, id);
        Set<Permission> dbPermissions = roles.stream().map(role -> permissionService.findByCode(PermissionCode.valueOf(role))).collect(Collectors.toSet());
        user.setPermissions(dbPermissions);
        update(principal, user);
    }

    @Override
    public Page<User> findAll(UserPrincipal principal, String firstName, String lastName, String username, String email,
                              String mobile, PageRequest pageRequest) {

        Page<User> userPage;

        if (!StringUtils.isEmpty(firstName)) {
            //search by first name
            userPage = userRepository.findByFirstNameContains(firstName, pageRequest);
        }
        else if(!StringUtils.isEmpty(lastName)) {
            //search by last name
            userPage = userRepository.findByLastNameContains(lastName, pageRequest);
        }
        else if(!StringUtils.isEmpty(username)) {
            //search by username
            userPage = userRepository.findByUsernameContains(username, pageRequest);
        }
        else if(!StringUtils.isEmpty(email)) {
            //search by username
            userPage = userRepository.findByEmailContains(email, pageRequest);
        }
        else if(!StringUtils.isEmpty(mobile)) {
            //search by username
            userPage = userRepository.findByMobileContains(mobile, pageRequest);
        }
        else{
            // search all
            userPage = userRepository.findAll(pageRequest);
        }

        return userPage;
    }

    @Override
    public User findById(UserPrincipal principal, String id) {
        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("User with id {} not found",id);
                    throw  new ResourceNotFoundException(String.format("User with id %s not found",id));
                }
        );
    }

    @Override
    public void deleteById(UserPrincipal principal, String id) {
        userRepository.delete(findById(principal, id));
    }

    @Override
    public long count() {
        return userRepository.count();
    }

}
