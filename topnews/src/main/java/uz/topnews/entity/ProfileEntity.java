package uz.topnews.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.topnews.enums.ProfileStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String username; // email or phone

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileStatus status;

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "photo_id")
    private Long photoId;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileRoleEntity> roles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        if (this.visible == null) this.visible = true;
        if (this.status == null) this.status = ProfileStatus.NOT_ACTIVE;
    }
}
