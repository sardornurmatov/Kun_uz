package uz.topnews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.topnews.entity.SectionEntity;

import java.util.List;

public interface SectionRepository extends JpaRepository<SectionEntity, Long> {

    Boolean existsByKey(String key);

    // Admin uchun - pagination bilan
    Page<SectionEntity> findAll(Pageable pageable);

    // Public uchun - faqat visible=true
    List<SectionEntity> findAllByVisibleTrueOrderByOrderNumberAsc();
}
