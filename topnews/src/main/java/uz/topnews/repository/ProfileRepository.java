package uz.topnews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.topnews.entity.ProfileEntity;
import uz.topnews.enums.ProfileRole;
import uz.topnews.enums.ProfileStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT p FROM ProfileEntity p " +
           "JOIN p.roles r " +
           "WHERE (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(p.surname) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(p.username) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:role IS NULL OR r.role = :role) " +
           "AND (:dateFrom IS NULL OR p.createdDate >= :dateFrom) " +
           "AND (:dateTo IS NULL OR p.createdDate <= :dateTo) " +
           "AND p.visible = true")
    Page<ProfileEntity> filterProfiles(
            @Param("query") String query,
            @Param("role") ProfileRole role,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );
}
