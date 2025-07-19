package ai.shreds.domain.value_objects;

import lombok.Builder;
import java.util.Objects;
import java.util.UUID;

/**
 * Command object to create a new Product aggregate.
 */
@Builder
public final class CreateProductCommand {
    private final String name;
    private final String description;
    private final String shortDescription;
    private final String brand;
    private final String model;
    private final UUID categoryId;
    private final DomainMoneyValue basePrice;
    private final DomainWeightValue weight;
    private final DomainDimensionsValue dimensions;

    public CreateProductCommand(
            String name,
            String description,
            String shortDescription,
            String brand,
            String model,
            UUID categoryId,
            DomainMoneyValue basePrice,
            DomainWeightValue weight,
            DomainDimensionsValue dimensions) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank() || name.length() > 255) {
            throw new IllegalArgumentException("Product name is required and must be <= 255 characters");
        }
        this.description = description;
        if (description != null && description.length() > 2000) {
            throw new IllegalArgumentException("Description must be <= 2000 characters");
        }
        this.shortDescription = shortDescription;
        if (shortDescription != null && shortDescription.length() > 500) {
            throw new IllegalArgumentException("Short description must be <= 500 characters");
        }
        this.brand = brand;
        if (brand != null && brand.length() > 100) {
            throw new IllegalArgumentException("Brand must be <= 100 characters");
        }
        this.model = model;
        if (model != null && model.length() > 100) {
            throw new IllegalArgumentException("Model must be <= 100 characters");
        }
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId must not be null");
        this.basePrice = Objects.requireNonNull(basePrice, "basePrice must not be null");
        this.weight = weight;
        this.dimensions = dimensions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public DomainMoneyValue getBasePrice() {
        return basePrice;
    }

    public DomainWeightValue getWeight() {
        return weight;
    }

    public DomainDimensionsValue getDimensions() {
        return dimensions;
    }
}