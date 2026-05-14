package uz.kun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kun.entity.Article;
import uz.kun.entity.enums.ArticleStatus;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    List<Article> findByStatusAndVisibleTrue(ArticleStatus status);
}
