package uz.kun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String status;
    private List<String> roles;
    private LocalDateTime createdDate;
    private String photoId;
}
