package cm.gelodia.pm.auth.service.impl;


import cm.gelodia.pm.auth.constant.AuthConstantType;
import cm.gelodia.pm.auth.model.*;
import cm.gelodia.pm.auth.payload.ResetPassword;
import cm.gelodia.pm.auth.payload.SignInRequest;
import cm.gelodia.pm.auth.payload.SignInResponse;
import cm.gelodia.pm.auth.payload.SignUpRequest;
import cm.gelodia.pm.auth.repository.UserRepository;
import cm.gelodia.pm.auth.repository.VerificationTokenRepository;
import cm.gelodia.pm.auth.security.JwtTokenProvider;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.auth.security.UsernamePasswordCompanyCodeAuthenticationToken;
import cm.gelodia.pm.auth.service.AuthService;
import cm.gelodia.pm.auth.service.PermissionService;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.company.service.CompanyService;
import cm.gelodia.pm.notification.constant.NotificationConstantType;
import cm.gelodia.pm.notification.model.Mail;
import cm.gelodia.pm.notification.model.MailState;
import cm.gelodia.pm.notification.model.MailTemplate;
import cm.gelodia.pm.notification.model.MailTemplateType;
import cm.gelodia.pm.notification.service.MailService;
import cm.gelodia.pm.notification.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final PermissionService permissionService;
    private final MailService mailService;
    private final MailTemplateService mailTemplateService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final Pattern bcryptPattern = Pattern.compile(AuthConstantType.BCRYPT_PATTERN);


    @Override
    @Transactional
    public User signUp(SignUpRequest signUpRequest) {

        Company company = companyService.findByCode(signUpRequest.getCompanyCode());
        if( company == null) {
            log.error("Le code client {} n'existe pas!",signUpRequest.getCompanyCode());
            throw new BadRequestException(String.format("Le code client %s n'existe pas!",signUpRequest.getCompanyCode()));
        }
        if(userRepository.checkByUsernameAndCompanyCode(signUpRequest.getUsername(), signUpRequest.getCompanyCode())) {
            log.error("Le login {} est déjà utilisé pour le société avec le code {}!",signUpRequest.getUsername(), signUpRequest.getCompanyCode());
            throw new BadRequestException(String.format("Le login %s est déjà utilisé pour le société avec le code %s!",signUpRequest.getUsername(), signUpRequest.getCompanyCode()));
        }

        if(userRepository.checkByEmailAndCompanyCode(signUpRequest.getEmail(), signUpRequest.getCompanyCode())) {
            log.error("L'email {} est déjà utilisé pour la société avec le code {}!",signUpRequest.getEmail(), signUpRequest.getCompanyCode());
            throw new BadRequestException(String.format("L'email %s est déjà utilisé pour la société avec le code %s!",signUpRequest.getEmail(), signUpRequest.getCompanyCode()));
        }

        // Creating user's account
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        if(!bcryptPattern.matcher(signUpRequest.getPassword()).matches()) {
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }else {
            user.setPassword("{bcrypt}" +  signUpRequest.getPassword());
        }
        user.setCompany(company);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        Permission permission = permissionService.findByCode(PermissionCode.ROLE_BAYER);
        user.setPermissions(Collections.singleton(permission));
        user = userRepository.save(user);
        // create principal
        UserPrincipal principal = UserPrincipal.create(user);
        //generate verification token and save it into database
        String token = generateVerificationToken(user);
        // find mail template for success signUp
        MailTemplate mailTemplate = mailTemplateService.findByType(principal, MailTemplateType.SIGN_UP_SUCCESS);
        // build mail and save in database
        String fullName = user.getFirstName() != null && !user.getFirstName().isEmpty() ? user.getFirstName() + " " + user.getLastName() : user.getLastName();
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/auth/verify-account/{token}")
                .buildAndExpand(token).toUri();

        String content = mailTemplate.getContent()
                .replace(NotificationConstantType.SEND_SIGN_UP_SUCCESS_MAIl_LOGIN, signUpRequest.getUsername())
                .replace(NotificationConstantType.SEND_SIGN_UP_SUCCESS_MAIl_PASSWORD, signUpRequest.getPassword())
                .replace(NotificationConstantType.SEND_SIGN_UP_SUCCESS_MAIl_EMAIL, signUpRequest.getEmail())
                .replace(NotificationConstantType.SEND_SIGN_UP_SUCCESS_MAIl_FULL_NAME, fullName)
                .replace(NotificationConstantType.SEND_SIGN_UP_SUCCESS_MAIL_LINK, uri.toString());

        Mail mail = Mail.builder()
                .subject(mailTemplate.getSubject())
                .emailTo(user.getEmail())
                .content(content)
                .state(MailState.TO_SEND)
                .relatedObjectId(user.getId())
                .relatedClass(User.class.getName())
                .company(principal.getCompany())
                .build();
        mail.setCreatedBy(user.getId());
        mailService.create(principal, mail);
        return user;
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();
        verificationToken.setCreatedBy(user.getId());
        verificationTokenRepository.save(
                verificationToken
        );
        return token;
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordCompanyCodeAuthenticationToken(
                        signInRequest.getUsernameOrEmail(),
                        signInRequest.getPassword(),
                        signInRequest.getCompanyCode()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        SignInResponse signInResponse = tokenProvider.getUserFromToken(jwt);
        signInResponse.setAuthorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return signInResponse;
    }

    @Override
    public void resetPassword(String userId, ResetPassword resetPassword) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("User with id {} not found",userId);
                    throw  new ResourceNotFoundException(String.format("User with id %s not found",userId));
                }
        );
        if(!passwordEncoder.matches(resetPassword.getOldPassword(), user.getPassword())) {
            log.error("your old password does't not matches!");
            throw new BadRequestException("your old password does't not matches!");
        }
        user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> {
                    log.error("Unable to find verification token for token {}", token);
                    throw new ResourceNotFoundException(String.format("Unable to find verification token for token %s", token));
                }
        );
        findUserAndEnable(verificationToken);
    }

    private void findUserAndEnable(VerificationToken verificationToken) {
        User user = userRepository.findById(verificationToken.getUser().getId()).orElseThrow(
                () -> {
                    log.error("Unable to find user with id  {}", verificationToken.getUser().getId());
                    throw new ResourceNotFoundException(String.format("Unable to find user with id  %s", verificationToken.getUser().getId()));
                }
        );
        user.setEnabled(true);
        userRepository.save(user);
    }

}
