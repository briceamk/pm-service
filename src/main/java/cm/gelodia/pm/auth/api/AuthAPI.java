package cm.gelodia.pm.auth.api;


import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.payload.ResetPassword;
import cm.gelodia.pm.auth.payload.SignInRequest;
import cm.gelodia.pm.auth.payload.SignUpRequest;
import cm.gelodia.pm.auth.service.AuthService;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthAPI {

    private final AuthService authService;
    private final ValidationErrorService validationErrorService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;

        User user = authService.signUp(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/auth/users/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(new ResponseApi(true, "User registered successfully"));

    }

    @PutMapping("/reset-password/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> resetPasswordUser(@PathVariable String userId, @Valid @RequestBody ResetPassword resetPassword,
                                               BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        authService.resetPassword(userId, resetPassword);
        return ResponseEntity.ok().body(new ResponseApi(true, "password updated successfully!"));
    }

    @GetMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok(new ResponseApi(true, "Account activated successfully"));
    }

}
