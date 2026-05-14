package uz.topnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username bo'sh bo'lishi mumkin emas")
    private String username;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    private String password;
}
