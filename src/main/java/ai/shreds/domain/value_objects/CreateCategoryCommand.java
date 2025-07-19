package ai.shreds.domain.value_objects;

import lombok.Builder;
import java.util.Objects;
import java.util.UUID;

/**
 * Command object to create a new Category aggregate.
 */
@Builder
public final class CreateCategoryCommand {
    private final String name;
    private final String description;
    private final UUID parentCategoryId;
    private final int displayOrder;

    public CreateCategoryCommand(
            String name,
            String description,
            UUID parentCategoryId,
            int displayOrder) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank() || name.length() > 200) {
            throw new IllegalArgumentException("Category name is required and must be <= 200 characters");
        }
        this.description = description;
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("Description must be <= 1000 characters");
        }
        this.parentCategoryId = parentCategoryId; // optional
        if (displayOrder < 0) {
            throw new IllegalArgumentException("displayOrder must be non-negative");
        }
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}