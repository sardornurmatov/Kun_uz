package uz.topnews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.topnews.entity.CategoryEntity;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Boolean existsByKey(String key);

    // Admin uchun - orderNumber bo'yicha
    List<CategoryEntity> findAllByOrderByOrderNumberAsc();

    // Public uchun - faqat visible=true
    List<CategoryEntity> findAllByVisibleTrueOrderByOrderNumberAsc();
}
