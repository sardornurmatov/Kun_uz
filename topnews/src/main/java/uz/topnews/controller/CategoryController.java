package uz.topnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.topnews.dto.request.CategoryRequest;
import uz.topnews.dto.response.*;
import uz.topnews.enums.Language;
import uz.topnews.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * POST /api/v1/category
     * Admin: Yangi kategoriya yaratish
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> create(
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    /**
     * PUT /api/v1/category/{id}
     * Admin: Kategoriyani tahrirlash
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    /**
     * DELETE /api/v1/category/{id}
     * Admin: Kategoriyani o'chirish
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

    /**
     * GET /api/v1/category/list
     * Admin: Barcha kategoriyalar (order_number asc)
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getList() {
        return ResponseEntity.ok(categoryService.getList());
    }

    /**
     * GET /api/v1/category/public?lang=uz
     * Public: Til bo'yicha kategoriyalar (faqat visible=true)
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<CategoryLangDTO>>> getByLang(
            @RequestParam(defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }
}
