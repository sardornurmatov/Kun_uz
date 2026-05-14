package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.topnews.dto.request.CategoryRequest;
import uz.topnews.dto.response.*;
import uz.topnews.entity.CategoryEntity;
import uz.topnews.enums.Language;
import uz.topnews.exception.AppException;
import uz.topnews.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ============================================================
    // 1. CREATE (ADMIN)
    // ============================================================
    public ApiResponse<CategoryResponseDTO> create(CategoryRequest request) {
        if (categoryRepository.existsByKey(request.getKey())) {
            throw new AppException("Bu key allaqachon mavjud: " + request.getKey());
        }

        CategoryEntity category = CategoryEntity.builder()
                .orderNumber(request.getOrderNumber())
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .key(request.getKey())
                .visible(true)
                .build();

        categoryRepository.save(category);
        return ApiResponse.ok(toDTO(category));
    }

    // ============================================================
    // 2. UPDATE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<CategoryResponseDTO> update(Long id, CategoryRequest request) {
        CategoryEntity category = findById(id);

        category.setOrderNumber(request.getOrderNumber());
        category.setNameUz(request.getNameUz());
        category.setNameRu(request.getNameRu());
        category.setNameEn(request.getNameEn());
        category.setKey(request.getKey());

        categoryRepository.save(category);
        return ApiResponse.ok(toDTO(category));
    }

    // ============================================================
    // 3. DELETE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<String> delete(Long id) {
        CategoryEntity category = findById(id);
        categoryRepository.delete(category);
        return ApiResponse.ok("Kategoriya o'chirildi");
    }

    // ============================================================
    // 4. GET LIST (ADMIN) - orderNumber asc
    // ============================================================
    public ApiResponse<List<CategoryResponseDTO>> getList() {
        List<CategoryEntity> categories = categoryRepository.findAllByOrderByOrderNumberAsc();
        List<CategoryResponseDTO> result = categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ApiResponse.ok(result);
    }

    // ============================================================
    // 5. GET BY LANG (PUBLIC)
    // ============================================================
    public ApiResponse<List<CategoryLangDTO>> getByLang(Language lang) {
        List<CategoryEntity> categories = categoryRepository.findAllByVisibleTrueOrderByOrderNumberAsc();
        List<CategoryLangDTO> result = categories.stream()
                .map(c -> CategoryLangDTO.builder()
                        .id(c.getId())
                        .orderNumber(c.getOrderNumber())
                        .key(c.getKey())
                        .name(getNameByLang(c, lang))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.ok(result);
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private CategoryEntity findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException("Kategoriya topilmadi: " + id));
    }

    private String getNameByLang(CategoryEntity category, Language lang) {
        return switch (lang) {
            case ru -> category.getNameRu();
            case en -> category.getNameEn();
            default -> category.getNameUz();
        };
    }

    private CategoryResponseDTO toDTO(CategoryEntity category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .orderNumber(category.getOrderNumber())
                .nameUz(category.getNameUz())
                .nameRu(category.getNameRu())
                .nameEn(category.getNameEn())
                .key(category.getKey())
                .visible(category.getVisible())
                .createdDate(category.getCreatedDate())
                .build();
    }
}
