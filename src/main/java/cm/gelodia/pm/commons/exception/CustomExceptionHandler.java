package cm.gelodia.pm.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected final ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                                         WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),  e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    protected final ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException e,
                                                                                   WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),  e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    protected final ResponseEntity<ErrorDetails> handleAppException(ApplicationException e,
                                                                    WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),  e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomUsernameNotFoundException.class)
    protected final ResponseEntity<ErrorDetails> handleCustomUsernameNotFoundException(CustomUsernameNotFoundException e,
                                                                    WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),  e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    protected final ResponseEntity<ErrorDetails> handleFileStorageException(FileStorageException e,
                                                                                WebRequest request) {
        ErrorDetails exceptionDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),  e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    protected final ResponseEntity<ErrorDetails> handleConflictException(ConflictException e,
                                                                             WebRequest request) {
        ErrorDetails exceptionDetails = new ErrorDetails(HttpStatus.CONFLICT.value(),
                LocalDateTime.now(), e.getLocalizedMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDetails, HttpStatus.CONFLICT);
    }

}
