package uz.topnews.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.topnews.enums.ProfileRole;

@Entity
@Table(name = "profile_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileRole role;
}
