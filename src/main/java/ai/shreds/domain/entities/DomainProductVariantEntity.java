package ai.shreds.domain.entities;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.Objects;
import ai.shreds.domain.value_objects.AddVariantCommand;

/**
 * Entity representing a product variant option (e.g., color, size).
 */
public class DomainProductVariantEntity {
    private UUID id;
    private UUID productId;
    private String variantType;
    private String variantValue;
    private String displayName;
    private Integer displayOrder;
    private Boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Default constructor for infrastructure mapping.
     */
    public DomainProductVariantEntity() {
        // Default constructor
    }

    /**
     * Constructs a new product variant from a command.
     */
    public DomainProductVariantEntity(AddVariantCommand command) {
        this.id = UUID.randomUUID();
        this.productId = Objects.requireNonNull(command.getProductId(), "productId must not be null");
        this.variantType = Objects.requireNonNull(command.getVariantType(), "variantType must not be null");
        this.variantValue = Objects.requireNonNull(command.getVariantValue(), "variantValue must not be null");
        this.displayName = command.getDisplayName();
        this.displayOrder = command.getDisplayOrder();
        this.isActive = true;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = this.createdAt;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getVariantType() { return variantType; }
    public String getVariantValue() { return variantValue; }
    public String getDisplayName() { return displayName; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean isActive() { return isActive; }
    public Boolean getIsActive() { return isActive; } // For mapper compatibility
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }

    // Public setters for infrastructure mapping
    public void setId(UUID id) { this.id = id; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public void setVariantType(String variantType) { this.variantType = variantType; }
    public void setVariantValue(String variantValue) { this.variantValue = variantValue; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Deactivates this variant.
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = ZonedDateTime.now();
    }
}