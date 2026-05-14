package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.topnews.dto.request.*;
import uz.topnews.dto.response.*;
import uz.topnews.entity.*;
import uz.topnews.enums.*;
import uz.topnews.exception.AppException;
import uz.topnews.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // ============================================================
    // 1. CREATE PROFILE (ADMIN) - Moderator yoki Publisher yaratish
    // ============================================================
    @Transactional
    public ApiResponse<ProfileInfoDTO> createProfile(ProfileCreateRequest request) {
        if (profileRepository.existsByUsername(request.getUsername())) {
            throw new AppException("Bu username allaqachon mavjud");
        }

        // Faqat MODERATOR va PUBLISHER yaratish mumkin
        for (ProfileRole role : request.getRoleList()) {
            if (role == ProfileRole.ROLE_USER || role == ProfileRole.ROLE_ADMIN) {
                throw new AppException("Admin faqat MODERATOR va PUBLISHER yarata oladi");
            }
        }

        ProfileEntity profile = ProfileEntity.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(request.getStatus())
                .visible(true)
                .build();

        profileRepository.save(profile);

        // Rollarni saqlash
        saveRoles(profile, request.getRoleList());

        return ApiResponse.ok(toDTO(profile));
    }

    // ============================================================
    // 2. GET BY ID (ADMIN)
    // ============================================================
    public ApiResponse<ProfileInfoDTO> getById(Long id) {
        ProfileEntity profile = findById(id);
        return ApiResponse.ok(toDTO(profile));
    }

    // ============================================================
    // 3. UPDATE PROFILE (ADMIN)
    // ============================================================
    @Transactional
    public ApiResponse<ProfileInfoDTO> updateProfile(Long id, ProfileUpdateRequest request) {
        ProfileEntity profile = findById(id);

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getSurname() != null) profile.setSurname(request.getSurname());
        if (request.getStatus() != null) profile.setStatus(request.getStatus());

        if (request.getRoleList() != null && !request.getRoleList().isEmpty()) {
            profileRoleRepository.deleteByProfileId(id);
            saveRoles(profile, request.getRoleList());
        }

        profileRepository.save(profile);
        return ApiResponse.ok(toDTO(profile));
    }

    // ============================================================
    // 4. UPDATE OWN PROFILE DETAIL (ANY)
    // ============================================================
    public ApiResponse<ProfileInfoDTO> updateDetail(String currentUsername,
                                                     ProfileDetailUpdateRequest request) {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(currentUsername)
                .orElseThrow(() -> new AppException("Foydalanuvchi topilmadi"));

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getSurname() != null) profile.setSurname(request.getSurname());

        profileRepository.save(profile);
        return ApiResponse.ok(toDTO(profile));
    }

    // ============================================================
    // 5. PROFILE LIST WITH PAGINATION (ADMIN)
    // ============================================================
    public ApiResponse<Page<ProfileInfoDTO>> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ProfileEntity> profiles = profileRepository.findAll(pageable);
        Page<ProfileInfoDTO> result = profiles.map(this::toDTO);
        return ApiResponse.ok(result);
    }

    // ============================================================
    // 6. DELETE PROFILE (ADMIN) - soft delete
    // ============================================================
    public ApiResponse<String> deleteById(Long id) {
        ProfileEntity profile = findById(id);
        profile.setVisible(false);
        profileRepository.save(profile);
        return ApiResponse.ok("Profil o'chirildi");
    }

    // ============================================================
    // 7. UPDATE PHOTO (ANY)
    // ============================================================
    public ApiResponse<String> updatePhoto(String currentUsername, Long photoId) {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(currentUsername)
                .orElseThrow(() -> new AppException("Foydalanuvchi topilmadi"));
        // Eski rasm o'chiriladi (Attach service orqali)
        profile.setPhotoId(photoId);
        profileRepository.save(profile);
        return ApiResponse.ok("Rasm yangilandi");
    }

    // ============================================================
    // 8. UPDATE PASSWORD (ANY)
    // ============================================================
    public ApiResponse<String> updatePassword(String currentUsername,
                                               PasswordUpdateRequest request) {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(currentUsername)
                .orElseThrow(() -> new AppException("Foydalanuvchi topilmadi"));

        if (!passwordEncoder.matches(request.getOldPassword(), profile.getPassword())) {
            throw new AppException("Eski parol noto'g'ri");
        }

        profile.setPassword(passwordEncoder.encode(request.getNewPassword()));
        profileRepository.save(profile);
        return ApiResponse.ok("Parol muvaffaqiyatli yangilandi");
    }

    // ============================================================
    // 9. FILTER (ADMIN)
    // ============================================================
    public ApiResponse<Page<ProfileInfoDTO>> filter(ProfileFilterRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<ProfileEntity> profiles = profileRepository.filterProfiles(
                request.getQuery(),
                request.getRole(),
                request.getCreatedDateFrom(),
                request.getCreatedDateTo(),
                pageable
        );

        return ApiResponse.ok(profiles.map(this::toDTO));
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private ProfileEntity findById(Long id) {
        return profileRepository.findById(id)
                .filter(ProfileEntity::getVisible)
                .orElseThrow(() -> new AppException("Profil topilmadi: " + id));
    }

    private void saveRoles(ProfileEntity profile, List<ProfileRole> roles) {
        roles.forEach(role -> {
            ProfileRoleEntity roleEntity = ProfileRoleEntity.builder()
                    .profile(profile)
                    .role(role)
                    .build();
            profileRoleRepository.save(roleEntity);
        });
    }

    public ProfileInfoDTO toDTO(ProfileEntity profile) {
        List<ProfileRole> roles = profile.getRoles().stream()
                .map(ProfileRoleEntity::getRole)
                .collect(Collectors.toList());

        return ProfileInfoDTO.builder()
                .id(profile.getId())
                .name(profile.getName())
                .surname(profile.getSurname())
                .username(profile.getUsername())
                .roleList(roles)
                .createdDate(profile.getCreatedDate())
                .status(profile.getStatus())
                .photoId(profile.getPhotoId())
                .build();
    }
}
