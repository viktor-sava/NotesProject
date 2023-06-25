package ua.lpnu.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.lpnu.notes.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select o.id from Note n join n.folder f join n.user o where f.id = :id")
    Long findOwnerIdOfFolder(Long id);
}
