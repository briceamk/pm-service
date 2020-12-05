package cm.gelodia.pm.auth.service;


import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.payload.ResetPassword;
import cm.gelodia.pm.auth.payload.SignInRequest;
import cm.gelodia.pm.auth.payload.SignInResponse;
import cm.gelodia.pm.auth.payload.SignUpRequest;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
    void resetPassword( String userId, ResetPassword resetPassword);
    void verifyAccount(String token);
}
