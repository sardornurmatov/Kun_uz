package uz.kun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kun.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
