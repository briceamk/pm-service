package cm.gelodia.pm.commons.service.impl;

import cm.gelodia.pm.commons.service.ValidationErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;

@Service("validationErrorService")
public class ValidationErrorServiceImpl implements ValidationErrorService {

    @Override
    public ResponseEntity<?> process(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
            return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}

