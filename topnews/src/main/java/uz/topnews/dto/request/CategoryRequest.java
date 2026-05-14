package uz.topnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotNull(message = "Tartib raqami bo'sh bo'lishi mumkin emas")
    private Integer orderNumber;

    @NotBlank(message = "O'zbekcha nom bo'sh bo'lishi mumkin emas")
    private String nameUz;

    @NotBlank(message = "Ruscha nom bo'sh bo'lishi mumkin emas")
    private String nameRu;

    @NotBlank(message = "Inglizcha nom bo'sh bo'lishi mumkin emas")
    private String nameEn;

    @NotBlank(message = "Key bo'sh bo'lishi mumkin emas")
    private String key;
}
