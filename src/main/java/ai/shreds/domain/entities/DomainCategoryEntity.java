package ai.shreds.domain.entities;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Core Category aggregate root entity.
 */
public class DomainCategoryEntity {
    private UUID id;
    private String name;
    private String description;
    private UUID parentCategoryId;
    private String path;
    private Integer level;
    private Integer displayOrder;
    private Boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Default constructor for infrastructure mapping.
     */
    public DomainCategoryEntity() {
        // Default constructor
    }

    /**
     * Constructs a new category from a create command.
     */
    public DomainCategoryEntity(ai.shreds.domain.value_objects.CreateCategoryCommand command) {
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(command.getName(), "name must not be null");
        this.description = command.getDescription();
        this.parentCategoryId = command.getParentCategoryId();
        this.displayOrder = command.getDisplayOrder();
        this.isActive = true;
        this.level = 0;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = this.createdAt;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public UUID getParentCategoryId() { return parentCategoryId; }
    public String getPath() { return path; }
    public Integer getLevel() { return level; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean isActive() { return isActive; }
    public Boolean getIsActive() { return isActive; } // For mapper compatibility
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }

    // Public setters for infrastructure mapping
    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setParentCategoryId(UUID parentCategoryId) { this.parentCategoryId = parentCategoryId; }
    public void setPath(String path) { this.path = path; }
    public void setLevel(Integer level) { this.level = level; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Computes and sets the hierarchical path based on parent path.
     */
    public void computePath(String parentPath) {
        if (parentPath == null || parentPath.isBlank()) {
            this.path = this.name;
        } else {
            this.path = parentPath + "/" + this.name;
        }
        touch();
    }

    /**
     * Computes and sets the level based on parent level.
     */
    public void computeLevel(Integer parentLevel) {
        this.level = parentLevel + 1;
        touch();
    }

    /**
     * Deactivates this category.
     */
    public void deactivate() {
        this.isActive = false;
        touch();
    }

    private void touch() {
        this.updatedAt = ZonedDateTime.now();
    }
}