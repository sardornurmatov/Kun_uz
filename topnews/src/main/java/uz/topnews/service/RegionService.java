package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.topnews.dto.request.RegionRequest;
import uz.topnews.dto.response.*;
import uz.topnews.entity.RegionEntity;
import uz.topnews.enums.Language;
import uz.topnews.exception.AppException;
import uz.topnews.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    // ============================================================
    // 1. CREATE (ADMIN)
    // ============================================================
    public ApiResponse<RegionResponseDTO> create(RegionRequest request) {
        if (regionRepository.existsByKey(request.getKey())) {
            throw new AppException("Bu key allaqachon mavjud: " + request.getKey());
        }

        RegionEntity region = RegionEntity.builder()
                .orderNumber(request.getOrderNumber())
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .key(request.getKey())
                .visible(true)
                .build();

        regionRepository.save(region);
        return ApiResponse.ok(toDTO(region));
    }

    // ============================================================
    // 2. UPDATE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<RegionResponseDTO> update(Long id, RegionRequest request) {
        RegionEntity region = findById(id);

        region.setOrderNumber(request.getOrderNumber());
        region.setNameUz(request.getNameUz());
        region.setNameRu(request.getNameRu());
        region.setNameEn(request.getNameEn());
        region.setKey(request.getKey());

        regionRepository.save(region);
        return ApiResponse.ok(toDTO(region));
    }

    // ============================================================
    // 3. DELETE BY ID (ADMIN)
    // ============================================================
    public ApiResponse<String> delete(Long id) {
        RegionEntity region = findById(id);
        regionRepository.delete(region);
        return ApiResponse.ok("Region o'chirildi");
    }

    // ============================================================
    // 4. GET LIST (ADMIN) - created_date desc
    // ============================================================
    public ApiResponse<List<RegionResponseDTO>> getList() {
        List<RegionEntity> regions = regionRepository.findAllByOrderByCreatedDateDesc();
        List<RegionResponseDTO> result = regions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ApiResponse.ok(result);
    }

    // ============================================================
    // 5. GET BY LANG (PUBLIC) - faqat visible=true, tilga mos nom
    // ============================================================
    public ApiResponse<List<RegionLangDTO>> getByLang(Language lang) {
        List<RegionEntity> regions = regionRepository.findAllByVisibleTrueOrderByOrderNumberAsc();
        List<RegionLangDTO> result = regions.stream()
                .map(r -> RegionLangDTO.builder()
                        .id(r.getId())
                        .key(r.getKey())
                        .name(getNameByLang(r, lang))
                        .build())
                .collect(Collectors.toList());
        return ApiResponse.ok(result);
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private RegionEntity findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new AppException("Region topilmadi: " + id));
    }

    private String getNameByLang(RegionEntity region, Language lang) {
        return switch (lang) {
            case ru -> region.getNameRu();
            case en -> region.getNameEn();
            default -> region.getNameUz();
        };
    }

    private RegionResponseDTO toDTO(RegionEntity region) {
        return RegionResponseDTO.builder()
                .id(region.getId())
                .orderNumber(region.getOrderNumber())
                .nameUz(region.getNameUz())
                .nameRu(region.getNameRu())
                .nameEn(region.getNameEn())
                .key(region.getKey())
                .visible(region.getVisible())
                .createdDate(region.getCreatedDate())
                .build();
    }
}
