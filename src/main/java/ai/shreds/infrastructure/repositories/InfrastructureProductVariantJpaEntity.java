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
    name = "product_variant",
    indexes = {
        @Index(name = "idx_variant_product_id", columnList = "product_id"),
        @Index(name = "idx_variant_type", columnList = "variant_type"),
        @Index(name = "idx_variant_active", columnList = "is_active"),
        @Index(name = "idx_variant_product_type_value", columnList = "product_id, variant_type, variant_value", unique = true),
        @Index(name = "idx_variant_display_order", columnList = "product_id, variant_type, display_order")
    }
)
public class InfrastructureProductVariantJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "variant_type", nullable = false, length = 50)
    private String variantType;

    @Column(name = "variant_value", nullable = false, length = 100)
    private String variantValue;

    @Column(name = "display_name", length = 100)
    private String displayName;

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
        if (displayName == null && variantValue != null) {
            displayName = variantValue;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}