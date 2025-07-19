package ai.shreds.domain.entities;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import ai.shreds.domain.value_objects.DomainMoneyValue;
import ai.shreds.domain.value_objects.DomainWeightValue;
import ai.shreds.domain.value_objects.DomainDimensionsValue;
import ai.shreds.domain.exceptions.DomainInvalidStatusTransitionException;

/**
 * SKU entity representing a particular configuration of a product.
 */
public class DomainSKUEntity {
    private UUID id;
    private String code;
    private UUID productId;
    private Map<String, String> variantCombination;
    private DomainMoneyValue price;
    private DomainWeightValue weight;
    private DomainDimensionsValue dimensions;
    private SharedProductStatusEnum status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Default constructor for infrastructure mapping.
     */
    public DomainSKUEntity() {
        // Default constructor
    }

    /**
     * Full constructor for creating new SKU entities.
     */
    public DomainSKUEntity(
            UUID id,
            String code,
            UUID productId,
            Map<String, String> variantCombination,
            DomainMoneyValue price,
            DomainWeightValue weight,
            DomainDimensionsValue dimensions,
            SharedProductStatusEnum status,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.variantCombination = variantCombination;
        this.price = price;
        this.weight = weight;
        this.dimensions = dimensions;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    // Getters
    public UUID getId() { return id; }
    public String getCode() { return code; }
    public UUID getProductId() { return productId; }
    public Map<String, String> getVariantCombination() { return variantCombination; }
    public DomainMoneyValue getPrice() { return price; }
    public DomainWeightValue getWeight() { return weight; }
    public DomainDimensionsValue getDimensions() { return dimensions; }
    public SharedProductStatusEnum getStatus() { return status; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }

    // Public setters for infrastructure mapping
    public void setId(UUID id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public void setVariantCombination(Map<String, String> variantCombination) { this.variantCombination = variantCombination; }
    public void setPrice(DomainMoneyValue price) { this.price = price; }
    public void setWeight(DomainWeightValue weight) { this.weight = weight; }
    public void setDimensions(DomainDimensionsValue dimensions) { this.dimensions = dimensions; }
    public void setStatus(SharedProductStatusEnum status) { this.status = status; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Validates SKU status against parent product status.
     */
    public void validateStatusWithProduct(SharedProductStatusEnum productStatus) {
        Objects.requireNonNull(productStatus, "productStatus must not be null");
        if (SharedProductStatusEnum.ACTIVE.equals(this.status)
                && !SharedProductStatusEnum.ACTIVE.equals(productStatus)) {
            throw new DomainInvalidStatusTransitionException(productStatus, this.status);
        }
    }

    /**
     * Returns effective price: override if present, otherwise use product base price.
     */
    public DomainMoneyValue getEffectivePrice(DomainMoneyValue productBasePrice) {
        if (this.price != null) {
            return this.price;
        }
        return Objects.requireNonNull(productBasePrice, "productBasePrice must not be null");
    }

    /**
     * Updates the status and touches the updated timestamp.
     */
    public void updateStatus(SharedProductStatusEnum newStatus) {
        this.status = Objects.requireNonNull(newStatus, "newStatus must not be null");
        this.updatedAt = ZonedDateTime.now();
    }
}