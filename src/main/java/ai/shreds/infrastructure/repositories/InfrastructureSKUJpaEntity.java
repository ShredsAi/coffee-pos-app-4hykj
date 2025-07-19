package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "sku",
    indexes = {
        @Index(name = "idx_sku_product_id", columnList = "product_id"),
        @Index(name = "idx_sku_code", columnList = "code", unique = true),
        @Index(name = "idx_sku_status", columnList = "status"),
        @Index(name = "idx_sku_created_at", columnList = "created_at")
    }
)
public class InfrastructureSKUJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "variant_combination", columnDefinition = "jsonb")
    private Map<String, String> variantCombination;

    @Column(name = "price_amount", precision = 19, scale = 4)
    private BigDecimal priceAmount;

    @Column(name = "price_currency", length = 3)
    private String priceCurrency;

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

    @Column(name = "status", nullable = false, length = 20)
    private String status;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
