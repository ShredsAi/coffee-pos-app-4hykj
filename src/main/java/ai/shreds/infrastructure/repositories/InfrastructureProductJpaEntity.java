package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product", indexes = {
    @Index(name = "idx_product_category_id", columnList = "category_id"),
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_status", columnList = "status"),
    @Index(name = "idx_product_brand_model", columnList = "brand, model"),
    @Index(name = "idx_product_created_at", columnList = "created_at")
})
public class InfrastructureProductJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "base_price_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal basePriceAmount;

    @Column(name = "base_price_currency", nullable = false, length = 3)
    private String basePriceCurrency;

    @Column(name = "weight_value", precision = 10, scale = 3)
    private BigDecimal weightValue;

    @Column(name = "weight_unit", length = 5)
    private String weightUnit;

    @Column(name = "dim_length", precision = 10, scale = 3)
    private BigDecimal dimLength;

    @Column(name = "dim_width", precision = 10, scale = 3)
    private BigDecimal dimWidth;

    @Column(name = "dim_height", precision = 10, scale = 3)
    private BigDecimal dimHeight;

    @Column(name = "dim_unit", length = 5)
    private String dimUnit;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

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
        if (version == null) {
            version = 0L;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}