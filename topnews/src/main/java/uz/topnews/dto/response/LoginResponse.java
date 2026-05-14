package uz.topnews.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.topnews.enums.ProfileRole;

import java.util.List;

@Data
@Builder
public class LoginResponse {
    private String name;
    private String surname;
    private String username;
    private List<ProfileRole> roleList;
    private PhotoDTO photo;
    private String token;

    @Data
    @Builder
    public static class PhotoDTO {
        private Long id;
        private String url;
    }
}
