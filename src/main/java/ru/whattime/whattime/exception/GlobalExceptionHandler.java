package ru.whattime.whattime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.whattime.whattime.dto.ErrorDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorDto.ErrorDtoBuilder createErrorBuilder(HttpStatus status, HttpServletRequest request) {
        return ErrorDto.builder()
                .error(status.getReasonPhrase())
                .status(status.value())
                .path(request.getServletPath())
                .timestamp(LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto defaultHandler(Exception exception, HttpServletRequest request) {
        var builder = createErrorBuilder(HttpStatus.INTERNAL_SERVER_ERROR, request);

        if (!exception.getMessage().isBlank()) {
            builder.message(exception.getMessage());
        }

        return builder.build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        var builder = createErrorBuilder(HttpStatus.BAD_REQUEST, request);

        var violations = exception.getConstraintViolations();
        if (violations != null) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            builder.message(message);
        }

        return builder.build();
    }
}
