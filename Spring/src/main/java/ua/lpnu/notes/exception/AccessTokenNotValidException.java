package ua.lpnu.notes.exception;

public class AccessTokenNotValidException extends RuntimeException {
    public AccessTokenNotValidException(String e) {
        super(e);
    }
}
