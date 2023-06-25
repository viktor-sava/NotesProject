package ua.lpnu.notes.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NoteDto(Long id, String content, String status, Date createdAt, Date updatedAt, FolderDto folder, List<TagDto> tags) {
}
