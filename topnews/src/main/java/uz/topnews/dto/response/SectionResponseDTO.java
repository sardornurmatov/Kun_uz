package uz.topnews.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SectionResponseDTO {
    private Long id;
    private Integer orderNumber;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String key;
    private Boolean visible;
    private Long imageId;
    private LocalDateTime createdDate;
}
