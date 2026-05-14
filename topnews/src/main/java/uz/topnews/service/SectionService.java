package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uz.topnews.dto.request.SectionRequest;
import uz.topnews.dto.response.*;
import uz.topnews.entity.SectionEntity;
import uz.topnews.enums.Language;
import uz.topnews.exception.AppException;
import uz.topnews.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    // ============================================================
    // 1. CREATE (ADMIN)
    // ============================================================
    public ApiResponse<SectionResponseDTO> create(SectionRequest request) {
        if (sectionRepository.existsByKey(request.getKey())) {
            throw new AppException("Bu key allaqachon mavjud: " + request.getKey());
        }

        SectionEntity section = SectionEntity.builder()
                .orderNumber(request.getOrderNumber())
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .key(request.getKey())
                .imageId(request.getImageId())
                .visible(true)
                .build();

        sectionRepository.save(section);
        return ApiResponse.ok(toDTO(section));
    }

    // ============================================================
    // 2. UPDATE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<SectionResponseDTO> update(Long id, SectionRequest request) {
        SectionEntity section = findById(id);

        section.setOrderNumber(request.getOrderNumber());
        section.setNameUz(request.getNameUz());
        section.setNameRu(request.getNameRu());
        section.setNameEn(request.getNameEn());
        section.setKey(request.getKey());
        if (request.getImageId() != null) {
            section.setImageId(request.getImageId());
        }

        sectionRepository.save(section);
        return ApiResponse.ok(toDTO(section));
    }

    // ============================================================
    // 3. DELETE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<String> delete(Long id) {
        SectionEntity section = findById(id);
        sectionRepository.delete(section);
        return ApiResponse.ok("Bo'lim o'chirildi");
    }

    // ============================================================
    // 4. GET LIST WITH PAGINATION (ADMIN)
    // ============================================================
    public ApiResponse<Page<SectionResponseDTO>> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SectionEntity> sections = sectionRepository.findAll(pageable);
        Page<SectionResponseDTO> result = sections.map(this::toDTO);
        return ApiResponse.ok(result);
    }

    // ============================================================
    // 5. GET BY LANG (PUBLIC)
    // ============================================================
    public ApiResponse<List<SectionLangDTO>> getByLang(Language lang) {
        List<SectionEntity> sections = sectionRepository.findAllByVisibleTrueOrderByOrderNumberAsc();
        List<SectionLangDTO> result = sections.stream()
                .map(s -> SectionLangDTO.builder()
                        .id(s.getId())
                        .key(s.getKey())
                        .name(getNameByLang(s, lang))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.ok(result);
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private SectionEntity findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new AppException("Bo'lim topilmadi: " + id));
    }

    private String getNameByLang(SectionEntity section, Language lang) {
        return switch (lang) {
            case ru -> section.getNameRu();
            case en -> section.getNameEn();
            default -> section.getNameUz();
        };
    }

    private SectionResponseDTO toDTO(SectionEntity section) {
        return SectionResponseDTO.builder()
                .id(section.getId())
                .orderNumber(section.getOrderNumber())
                .nameUz(section.getNameUz())
                .nameRu(section.getNameRu())
                .nameEn(section.getNameEn())
                .key(section.getKey())
                .visible(section.getVisible())
                .imageId(section.getImageId())
                .createdDate(section.getCreatedDate())
                .build();
    }
}
