package ua.lpnu.notes.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.lpnu.notes.exception.AccessTokenNotValidException;
import ua.lpnu.notes.exception.BadPasswordException;
import ua.lpnu.notes.exception.DuplicateEntityException;
import ua.lpnu.notes.exception.RefreshTokenNotValidException;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController  {

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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return new ExceptionResponse(message, HttpStatus.BAD_REQUEST);
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
