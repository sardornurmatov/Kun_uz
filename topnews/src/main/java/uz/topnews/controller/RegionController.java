package uz.topnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.topnews.dto.request.RegionRequest;
import uz.topnews.dto.response.*;
import uz.topnews.enums.Language;
import uz.topnews.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    /**
     * POST /api/v1/region
     * Admin: Yangi region yaratish
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RegionResponseDTO>> create(
            @Valid @RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.create(request));
    }

    /**
     * PUT /api/v1/region/{id}
     * Admin: Regionni tahrirlash
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RegionResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.update(id, request));
    }

    /**
     * DELETE /api/v1/region/{id}
     * Admin: Regionni o'chirish
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.delete(id));
    }

    /**
     * GET /api/v1/region/list
     * Admin: Barcha regionlar (created_date desc)
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<RegionResponseDTO>>> getList() {
        return ResponseEntity.ok(regionService.getList());
    }

    /**
     * GET /api/v1/region/public?lang=uz
     * Public: Til bo'yicha regionlar (faqat visible=true)
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<RegionLangDTO>>> getByLang(
            @RequestParam(defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }
}
