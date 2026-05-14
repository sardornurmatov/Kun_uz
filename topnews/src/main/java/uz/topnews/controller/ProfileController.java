package uz.topnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.topnews.dto.request.*;
import uz.topnews.dto.response.*;
import uz.topnews.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * POST /api/v1/profile
     * Admin: Moderator yoki Publisher yaratish
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProfileInfoDTO>> create(
            @Valid @RequestBody ProfileCreateRequest request) {
        return ResponseEntity.ok(profileService.createProfile(request));
    }

    /**
     * GET /api/v1/profile/{id}
     * Admin: Id bo'yicha profilni ko'rish
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProfileInfoDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getById(id));
    }

    /**
     * PUT /api/v1/profile/{id}
     * Admin: Profilni tahrirlash
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProfileInfoDTO>> update(
            @PathVariable Long id,
            @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    /**
     * PUT /api/v1/profile/detail
     * Har qanday foydalanuvchi o'z ma'lumotlarini tahrirlash
     */
    @PutMapping("/detail")
    public ResponseEntity<ApiResponse<ProfileInfoDTO>> updateDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProfileDetailUpdateRequest request) {
        return ResponseEntity.ok(
                profileService.updateDetail(userDetails.getUsername(), request));
    }

    /**
     * GET /api/v1/profile/list?page=0&size=10
     * Admin: Barcha profillar ro'yxati (pagination)
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<ProfileInfoDTO>>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(profileService.getList(page, size));
    }

    /**
     * DELETE /api/v1/profile/{id}
     * Admin: Profilni o'chirish (soft delete)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.deleteById(id));
    }

    /**
     * PATCH /api/v1/profile/photo?photoId=5
     * Har qanday foydalanuvchi o'z rasmini yangilash
     */
    @PatchMapping("/photo")
    public ResponseEntity<ApiResponse<String>> updatePhoto(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long photoId) {
        return ResponseEntity.ok(
                profileService.updatePhoto(userDetails.getUsername(), photoId));
    }

    /**
     * PATCH /api/v1/profile/password
     * Har qanday foydalanuvchi o'z parolini yangilash
     */
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordUpdateRequest request) {
        return ResponseEntity.ok(
                profileService.updatePassword(userDetails.getUsername(), request));
    }

    /**
     * POST /api/v1/profile/filter
     * Admin: Filter bilan profillarni qidirish
     */
    @PostMapping("/filter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<ProfileInfoDTO>>> filter(
            @RequestBody ProfileFilterRequest request) {
        return ResponseEntity.ok(profileService.filter(request));
    }
}
