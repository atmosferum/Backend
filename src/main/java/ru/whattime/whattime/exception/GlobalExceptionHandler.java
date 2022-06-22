package ru.whattime.whattime.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import ru.whattime.whattime.dto.ErrorDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> responseStatusExceptionHandler(ResponseStatusException exception, HttpServletRequest request) {
        var body = ErrorDto.builder()
                .status(exception.getRawStatusCode())
                .error(exception.getStatus().getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(exception.getRawStatusCode()).body(body);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        var error = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
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
