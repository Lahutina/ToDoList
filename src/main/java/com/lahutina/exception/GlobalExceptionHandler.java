package com.lahutina.exception;

import com.lahutina.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NullEntityReferenceException.class, IncorrectPasswordException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> badRequestHandler(Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(),
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> notFoundHandler(Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(),
                exception.getMessage()), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> internalServerErrorHandler(Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
                exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiError> accessDeniedErrorHandler(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(),
                exception.getMessage()), HttpStatus.FORBIDDEN);
    }
}
