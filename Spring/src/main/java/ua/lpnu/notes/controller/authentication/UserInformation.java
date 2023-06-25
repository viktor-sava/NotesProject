package ua.lpnu.notes.controller.authentication;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInformation(@NotBlank(message = "First name is mandatory") String firstName,
                              @NotBlank(message = "Last name is mandatory") String lastName,
                              @Email(message = "Email should look like example@mail.com") String email,
                              @NotBlank(message = "Password is mandatory") @Min(value = 12, message = "Password length should be more then 10 characters")
                              String password) {
}

