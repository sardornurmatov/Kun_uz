package uz.topnews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.topnews.entity.EmailHistoryEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Long> {

    List<EmailHistoryEntity> findByEmailOrderByCreatedDateDesc(String email);

    List<EmailHistoryEntity> findByCreatedDateBetweenOrderByCreatedDateDesc(
            LocalDateTime from, LocalDateTime to);

    // 1 daqiqa ichida nechta email yuborilganini tekshirish (limit uchun)
    Long countByEmailAndCreatedDateAfter(String email, LocalDateTime after);

    Page<EmailHistoryEntity> findAll(Pageable pageable);
}
