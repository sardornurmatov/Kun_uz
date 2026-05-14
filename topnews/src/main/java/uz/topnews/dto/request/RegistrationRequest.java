package uz.topnews.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
    private String name;

    @NotBlank(message = "Familiya bo'sh bo'lishi mumkin emas")
    private String surname;

    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    @Email(message = "Email format noto'g'ri")
    private String username;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    @Size(min = 6, message = "Parol kamida 6 ta belgi bo'lishi kerak")
    private String password;
}
