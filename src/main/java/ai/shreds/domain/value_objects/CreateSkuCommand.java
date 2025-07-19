package ai.shreds.domain.value_objects;

import lombok.Builder;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Command object to create a new SKU under a Product aggregate.
 */
@Builder
public final class CreateSkuCommand {
    private final UUID productId;
    private final Map<String, String> variantCombination;
    private final DomainMoneyValue price;
    private final DomainWeightValue weight;
    private final DomainDimensionsValue dimensions;

    public CreateSkuCommand(
            UUID productId,
            Map<String, String> variantCombination,
            DomainMoneyValue price,
            DomainWeightValue weight,
            DomainDimensionsValue dimensions) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        // variantCombination may be null or empty
        this.variantCombination = variantCombination;
        // price, weight, dimensions are optional overrides
        this.price = price;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    public UUID getProductId() {
        return productId;
    }

    public Map<String, String> getVariantCombination() {
        return variantCombination;
    }

    public DomainMoneyValue getPrice() {
        return price;
    }

    public DomainWeightValue getWeight() {
        return weight;
    }

    public DomainDimensionsValue getDimensions() {
        return dimensions;
    }
}
