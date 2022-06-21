package ru.whattime.whattime.conntrolleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class ExceptionsControllerAdvice {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String errorMessage;

        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" " + violation.getMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occured.";
        }

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
