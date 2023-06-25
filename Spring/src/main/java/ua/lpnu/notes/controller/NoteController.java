package ua.lpnu.notes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.notes.dto.FolderDto;
import ua.lpnu.notes.dto.NoteDto;
import ua.lpnu.notes.dto.TagDto;
import ua.lpnu.notes.entity.Folder;
import ua.lpnu.notes.entity.Note;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.service.NoteService;
import ua.lpnu.notes.service.TagService;

import java.util.List;

@RestController
@RequestMapping("notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final TagService tagService;

    @PutMapping("{id}/folder")
    @PreAuthorize("@noteService.isOwner(authentication, #id) && @folderService.isOwner(authentication, #folderDto.id())")
    public NoteDto changeFolder(@RequestBody FolderDto folderDto, @PathVariable Long id) {
        return mapper(noteService.changeFolder(id, folderDto.id()));
    }

    @PostMapping("{id}/folder")
    @PreAuthorize("@noteService.isOwner(authentication, #id)")
    public NoteDto createFolder(@RequestBody FolderDto folderDto, @PathVariable Long id) {
        return mapper(noteService.createFolder(id, folderDto.name()));
    }

    @DeleteMapping("{id}/folder")
    @PreAuthorize("@noteService.isOwner(authentication, #id)")
    public NoteDto removeFolder(@PathVariable Long id) {
        return mapper(noteService.removeFolder(id));
    }

    @GetMapping
    public List<NoteDto> getNotes(Authentication principal) {
        return noteService.getNotes((User) principal.getPrincipal())
                .stream()
                .map(this::mapper)
                .toList();
    }

    @PutMapping
    @PreAuthorize("@noteService.isOwner(authentication, #noteDto.id())")
    public NoteDto changeNote(@RequestBody NoteDto noteDto) {
        Note note = noteService.changeNote(noteDto.id(), noteDto.content(), noteDto.status());
        return mapper(note);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("@noteService.isOwner(authentication, #id)")
    public void removeNote(@PathVariable("id") Long id) {
        noteService.removeNote(id);
    }

    @PostMapping
    public NoteDto createNote(Authentication principal) {
        Note note = noteService.createNote((User) principal.getPrincipal());
        return mapper(note);
    }

    @PostMapping("{id}/tags")
    @PreAuthorize("@noteService.isOwner(authentication, #id)")
    public TagDto addTagToNote(@PathVariable("id") Long id, @RequestBody TagDto tagDto) {
        return tagService.createTag(id, tagDto.value());
    }

    @DeleteMapping("{noteId}/tags/{tagId}")
    @PreAuthorize("@tagService.isOwner(authentication, #tagId)")
    public void removeTagToNote(@PathVariable Long noteId, @PathVariable Long tagId) {
        tagService.removeTag(tagId);
    }

    private NoteDto mapper(Note note) {
        List<TagDto> tags = note.getTags()
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .toList();
        Folder folder = note.getFolder();
        FolderDto folderDto;
        if (folder == null) {
            folderDto = null;
        } else {
            folderDto = new FolderDto(folder.getId(), folder.getName());
        }
        return new NoteDto(note.getId(), note.getContent(), note.getStatus(), note.getCreatedAt(), note.getUpdatedAt(), folderDto, tags);
    }
}
