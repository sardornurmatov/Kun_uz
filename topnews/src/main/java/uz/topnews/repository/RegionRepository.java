package uz.topnews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.topnews.entity.RegionEntity;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    Boolean existsByKey(String key);

    // Admin uchun - barcha regionlar, created_date desc
    List<RegionEntity> findAllByOrderByCreatedDateDesc();

    // Public uchun - faqat visible=true bo'lganlar, orderNumber bo'yicha
    List<RegionEntity> findAllByVisibleTrueOrderByOrderNumberAsc();
}
