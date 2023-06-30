package ua.lpnu.notes.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.validator.constraints.Length;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FolderDto(Long id,
                        @Length(min = 1, max = 30, message = "Folder name length should be between 5 and 30") String name) {
}
