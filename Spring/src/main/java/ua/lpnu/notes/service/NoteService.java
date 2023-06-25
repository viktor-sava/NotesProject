package ua.lpnu.notes.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.lpnu.notes.entity.Folder;
import ua.lpnu.notes.entity.Note;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.repository.NoteRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final FolderService folderService;

    public boolean isOwner(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        Long ownerId = noteRepository.findOwnerIdOfNote(id);
        return userId.equals(ownerId);
    }

    public List<Note> getNotes(User user) {
        return noteRepository.findAllByUser(user);
    }

    @Transactional
    public Note createNote(User user) {
        Note note = new Note();
        note.setUser(user);
        note.setCreatedAt(Date.from(Instant.now()));
        note.setStatus("default");
        return noteRepository.save(note);
    }

    @Transactional
    public Note changeNote(Long id, String content, String status) {
        Note note = getNote(id);
        note.setUpdatedAt(Date.from(Instant.now()));
        note.setContent(content);
        note.setStatus(status);
        return noteRepository.save(note);
    }

    @Transactional
    public Note changeNoteStatus(Long id, String status) {
        Note note = getNote(id);
        note.setUpdatedAt(Date.from(Instant.now()));
        note.setStatus(status);
        return noteRepository.save(note);
    }

    @Transactional
    public void removeNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Transactional
    public Note changeFolder(Long id, Long folderId) {
        Note note = getNote(id);
        Long oldFolderId = getFolderId(note);
        Folder folder = folderService.getFolder(folderId);

        note.setFolder(folder);
        Note saved = noteRepository.saveAndFlush(note);

        folderService.removeFolderIfNeeded(oldFolderId);

        return saved;
    }

    private Long getFolderId(Note note) {
        Folder folder = note.getFolder();
        if (folder != null) {
            return folder.getId();
        } else {
            return null;
        }
    }

    @Transactional
    public Note createFolder(Long id, String name) {
        Note note = getNote(id);
        Long oldFolderId = getFolderId(note);
        Folder folder = folderService.createFolder(name);

        note.setFolder(folder);
        Note saved = noteRepository.saveAndFlush(note);

        folderService.removeFolderIfNeeded(oldFolderId);

        return saved;
    }

    @Transactional
    public Note removeFolder(Long id) {
        Note note = getNote(id);
        Long oldFolderId = getFolderId(note);

        note.setFolder(null);
        Note saved = noteRepository.saveAndFlush(note);

        folderService.removeFolderIfNeeded(oldFolderId);

        return saved;
    }

    public Note getNote(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note with id %s is not found".formatted(id)));
    }
}
