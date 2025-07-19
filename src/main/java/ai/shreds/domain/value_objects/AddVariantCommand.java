package ai.shreds.domain.value_objects;

import java.util.Objects;
import java.util.UUID;

/**
 * Command object to add a new variant option to a Product aggregate.
 */
public final class AddVariantCommand {
    private final UUID productId;
    private final String variantType;
    private final String variantValue;
    private final String displayName;
    private final int displayOrder;

    public AddVariantCommand(
            UUID productId,
            String variantType,
            String variantValue,
            String displayName,
            int displayOrder) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.variantType = Objects.requireNonNull(variantType, "variantType must not be null");
        this.variantValue = Objects.requireNonNull(variantValue, "variantValue must not be null");
        this.displayName = displayName;
        this.displayOrder = displayOrder;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getVariantType() {
        return variantType;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}