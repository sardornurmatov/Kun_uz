package uz.kun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Long sharedCount;
    private String imageId;
    private Long regionId;
    private String moderatorId;
    private String publisherId;
    private String status;
    private Integer readTime;
    private Long viewCount;
    private Boolean visible;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private List<Long> categoryIds;
    private List<Long> sectionIds;
}
