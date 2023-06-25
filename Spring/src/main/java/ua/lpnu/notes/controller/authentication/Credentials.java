package ua.lpnu.notes.controller.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Credentials(@Email(message = "Email should look like example@mail.com") String email,
                          @NotBlank(message = "Password is mandatory") String password) {
}
