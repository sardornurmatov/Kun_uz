package uz.topnews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryLangDTO {
    private Long id;
    private Integer orderNumber;
    private String key;
    private String name;
}
