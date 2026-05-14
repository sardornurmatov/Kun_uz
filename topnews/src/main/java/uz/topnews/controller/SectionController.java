package uz.topnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.topnews.dto.request.SectionRequest;
import uz.topnews.dto.response.*;
import uz.topnews.enums.Language;
import uz.topnews.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    /**
     * POST /api/v1/section
     * Admin: Yangi bo'lim yaratish
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<SectionResponseDTO>> create(
            @Valid @RequestBody SectionRequest request) {
        return ResponseEntity.ok(sectionService.create(request));
    }

    /**
     * PUT /api/v1/section/{id}
     * Admin: Bo'limni tahrirlash
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<SectionResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody SectionRequest request) {
        return ResponseEntity.ok(sectionService.update(id, request));
    }

    /**
     * DELETE /api/v1/section/{id}
     * Admin: Bo'limni o'chirish
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(sectionService.delete(id));
    }

    /**
     * GET /api/v1/section/list?page=0&size=10
     * Admin: Barcha bo'limlar (pagination bilan)
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<SectionResponseDTO>>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(sectionService.getList(page, size));
    }

    /**
     * GET /api/v1/section/public?lang=uz
     * Public: Til bo'yicha bo'limlar (faqat visible=true)
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<SectionLangDTO>>> getByLang(
            @RequestParam(defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(sectionService.getByLang(lang));
    }
}
