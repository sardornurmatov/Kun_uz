package uz.topnews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.topnews.entity.ProfileRoleEntity;

import java.util.List;

public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity, Long> {

    List<ProfileRoleEntity> findByProfileId(Long profileId);

    void deleteByProfileId(Long profileId);
}
