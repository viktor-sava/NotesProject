package ua.lpnu.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.lpnu.notes.entity.Note;
import ua.lpnu.notes.entity.User;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("select o.id from Note n join n.user o where n.id = :id")
    Long findOwnerIdOfNote(Long id);

    List<Note> findAllByUser(User user);
}
