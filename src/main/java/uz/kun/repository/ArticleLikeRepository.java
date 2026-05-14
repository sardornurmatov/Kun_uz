package uz.kun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kun.entity.ArticleLike;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
}
