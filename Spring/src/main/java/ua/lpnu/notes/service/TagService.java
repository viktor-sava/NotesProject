package ua.lpnu.notes.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.lpnu.notes.dto.TagDto;
import ua.lpnu.notes.entity.Note;
import ua.lpnu.notes.entity.Tag;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final NoteService noteService;

    @Transactional
    public TagDto createTag(Long noteId, String value) {
        Note note = noteService.getNote(noteId);
        Tag tag = tagRepository.save(new Tag(value, note));
        return new TagDto(tag.getId(), tag.getName());
    }

    @Transactional
    public void removeTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    public boolean isOwner(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        Long ownerId = tagRepository.findOwnerIdOfTag(id);
        return userId.equals(ownerId);
    }
}
