package ua.lpnu.notes.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.validator.constraints.Length;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TagDto(Long id,
                     @Length(min = 1, max = 30, message = "Tag value length should be between 1 and 30") String value) {
}
