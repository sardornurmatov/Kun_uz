package uz.topnews.dto.request;

import lombok.Data;
import uz.topnews.enums.ProfileRole;
import uz.topnews.enums.ProfileStatus;

import java.util.List;

@Data
public class ProfileUpdateRequest {
    private String name;
    private String surname;
    private ProfileStatus status;
    private List<ProfileRole> roleList;
}
