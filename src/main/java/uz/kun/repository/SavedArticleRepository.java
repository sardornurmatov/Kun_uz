package uz.kun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kun.entity.SavedArticle;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {
}
