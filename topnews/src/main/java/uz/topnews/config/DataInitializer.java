package uz.topnews.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.topnews.entity.ProfileEntity;
import uz.topnews.entity.ProfileRoleEntity;
import uz.topnews.enums.ProfileRole;
import uz.topnews.enums.ProfileStatus;
import uz.topnews.repository.ProfileRepository;
import uz.topnews.repository.ProfileRoleRepository;



@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createAdminIfNotExists();
    }

    private void createAdminIfNotExists() {
        String adminEmail = "admin@topnews.uz";

        if (!profileRepository.existsByUsername(adminEmail)) {
            ProfileEntity admin = ProfileEntity.builder()
                    .name("Admin")
                    .surname("TopNews")
                    .username(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .status(ProfileStatus.ACTIVE)
                    .visible(true)
                    .build();

            profileRepository.save(admin);

            ProfileRoleEntity adminRole = ProfileRoleEntity.builder()
                    .profile(admin)
                    .role(ProfileRole.ROLE_ADMIN)
                    .build();

            profileRoleRepository.save(adminRole);

            log.info("==============================================");
            log.info("Admin yaratildi:");
            log.info("  Email   : {}", adminEmail);
            log.info("  Parol   : admin123");
            log.info("==============================================");
        }
    }
}
