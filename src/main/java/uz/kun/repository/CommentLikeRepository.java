package uz.kun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kun.entity.CommentLike;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
