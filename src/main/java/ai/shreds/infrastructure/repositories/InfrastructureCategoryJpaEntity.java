package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "category",
    indexes = {
        @Index(name = "idx_category_parent_id", columnList = "parent_category_id"),
        @Index(name = "idx_category_path", columnList = "path"),
        @Index(name = "idx_category_level", columnList = "level"),
        @Index(name = "idx_category_parent_name", columnList = "parent_category_id, name", unique = true),
        @Index(name = "idx_category_active", columnList = "is_active"),
        @Index(name = "idx_category_display_order", columnList = "parent_category_id, display_order")
    }
)
public class InfrastructureCategoryJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "parent_category_id")
    private UUID parentCategoryId;

    @Column(name = "path", nullable = false, length = 500)
    private String path;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        ZonedDateTime now = ZonedDateTime.now();
        createdAt = now;
        updatedAt = now;
        
        if (displayOrder == null) {
            displayOrder = 0;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (level == null) {
            level = 0;
        }
        if (path == null) {
            path = "/";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}