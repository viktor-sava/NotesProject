package ua.lpnu.notes.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.lpnu.notes.entity.Folder;
import ua.lpnu.notes.entity.Note;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.repository.FolderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public Folder getFolder(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Folder with id %s is not found".formatted(id)));
    }

    @Transactional
    public Folder createFolder(String name) {
        return folderRepository.save(new Folder(name));
    }

    public boolean isOwner(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        Long ownerId = folderRepository.findOwnerIdOfFolder(id);
        return userId.equals(ownerId);
    }

    @Transactional
    public void removeFolderIfNeeded(Long id) {
        if (id != null) {
            Folder folder = getFolder(id);
            List<Note> notes = folder.getNotes();
            if (notes.size() == 0) {
                folderRepository.deleteById(id);
            }
        }
    }
}
