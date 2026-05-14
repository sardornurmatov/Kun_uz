package uz.topnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificationRequest {

    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    private String email;

    @NotBlank(message = "Kod bo'sh bo'lishi mumkin emas")
    private String code;
}
