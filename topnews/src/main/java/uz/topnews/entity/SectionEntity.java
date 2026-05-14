package uz.topnews.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "section")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(unique = true)
    private String key;

    @Column(nullable = false)
    private Boolean visible = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "image_id")
    private Long imageId; // optional

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        if (this.visible == null) this.visible = true;
    }
}
