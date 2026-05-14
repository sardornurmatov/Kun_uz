package uz.kun.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.kun.entity.enums.ProfileRoleEnum;

@Data
@Entity
@Table(name = "profile_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileRoleEnum role;
}
