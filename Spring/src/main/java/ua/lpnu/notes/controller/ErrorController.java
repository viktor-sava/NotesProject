package ua.lpnu.notes.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.lpnu.notes.exception.AccessTokenNotValidException;
import ua.lpnu.notes.exception.BadPasswordException;
import ua.lpnu.notes.exception.DuplicateEntityException;
import ua.lpnu.notes.exception.RefreshTokenNotValidException;

import java.time.Instant;
import java.util.Date;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler  {

    record ExceptionResponse(String message, HttpStatus status, int code, Date date) {
        public ExceptionResponse(String message, HttpStatus status) {
            this(message, status, status.value(), Date.from(Instant.now()));
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessTokenNotValidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAccessTokenNotValidException(AccessTokenNotValidException e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EntityNotFoundException.class, BadPasswordException.class, DuplicateEntityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleEntityNotFoundException(Exception e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenNotValidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleRefreshTokenNotValidException(RefreshTokenNotValidException e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

}
