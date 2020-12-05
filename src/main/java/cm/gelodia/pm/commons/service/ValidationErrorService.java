package cm.gelodia.pm.commons.service;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface ValidationErrorService {
    public ResponseEntity<?> process(BindingResult result);
}
