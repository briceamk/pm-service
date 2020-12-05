package cm.gelodia.pm.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomUsernameNotFoundException extends UsernameNotFoundException {
    public CustomUsernameNotFoundException(String msg) {
        super(msg);
    }

    public CustomUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
