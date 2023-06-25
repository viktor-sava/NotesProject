package ua.lpnu.notes.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NoteDto(Long id,
                      @Length(max = 8000, message = "Note content length should not be more than 8000 characters") String content,
                      String status, Date createdAt, Date updatedAt, FolderDto folder, List<TagDto> tags) {
}
