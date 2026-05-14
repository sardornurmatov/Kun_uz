package uz.topnews.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.topnews.enums.ProfileRole;
import uz.topnews.enums.ProfileStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProfileInfoDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private List<ProfileRole> roleList;
    private LocalDateTime createdDate;
    private ProfileStatus status;
    private Long photoId;
}
