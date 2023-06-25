package ua.lpnu.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.lpnu.notes.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select o.id from Tag t join t.note n join n.user o where t.id = :id")
    Long findOwnerIdOfTag(Long id);
}
