package uz.topnews.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.topnews.enums.ProfileRole;
import uz.topnews.enums.ProfileStatus;

import java.util.List;

@Data
public class ProfileCreateRequest {

    @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
    private String name;

    @NotBlank(message = "Familiya bo'sh bo'lishi mumkin emas")
    private String surname;

    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    @Email(message = "Email format noto'g'ri")
    private String username;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    private String password;

    @NotNull(message = "Status bo'sh bo'lishi mumkin emas")
    private ProfileStatus status;

    @NotNull(message = "Rol bo'sh bo'lishi mumkin emas")
    private List<ProfileRole> roleList;
}
