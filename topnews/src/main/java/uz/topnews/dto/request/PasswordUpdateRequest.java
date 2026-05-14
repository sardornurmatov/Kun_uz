package uz.topnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

    @NotBlank(message = "Eski parol bo'sh bo'lishi mumkin emas")
    private String oldPassword;

    @NotBlank(message = "Yangi parol bo'sh bo'lishi mumkin emas")
    @Size(min = 6, message = "Parol kamida 6 ta belgi bo'lishi kerak")
    private String newPassword;
}
