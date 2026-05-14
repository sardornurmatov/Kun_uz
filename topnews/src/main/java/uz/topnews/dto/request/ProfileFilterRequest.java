package uz.topnews.dto.request;

import lombok.Data;
import uz.topnews.enums.ProfileRole;

import java.time.LocalDateTime;

@Data
public class ProfileFilterRequest {
    private String query;             // name / surname / username search
    private ProfileRole role;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private Integer page;
    private Integer size;
}
