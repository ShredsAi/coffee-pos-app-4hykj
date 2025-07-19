package ai.shreds.domain.entities;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.Objects;
import ai.shreds.domain.value_objects.CreateProductCommand;
import ai.shreds.domain.value_objects.UpdateProductCommand;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import ai.shreds.domain.value_objects.DomainMoneyValue;
import ai.shreds.domain.value_objects.DomainWeightValue;
import ai.shreds.domain.value_objects.DomainDimensionsValue;

/**
 * Core Product aggregate root entity.
 */
public class DomainProductEntity {
    private UUID id;
    private String name;
    private String description;
    private String shortDescription;
    private String brand;
    private String model;
    private SharedProductStatusEnum status;
    private UUID categoryId;
    private DomainMoneyValue basePrice;
    private DomainWeightValue weight;
    private DomainDimensionsValue dimensions;
    private Long version;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Default constructor for infrastructure mapping.
     */
    public DomainProductEntity() {
        // Default constructor
    }

    /**
     * Constructs a new product from a create command.
     */
    public DomainProductEntity(CreateProductCommand command) {
        this.id = UUID.randomUUID();
        setName(command.getName());
        setDescription(command.getDescription());
        setShortDescription(command.getShortDescription());
        setBrand(command.getBrand());
        setModel(command.getModel());
        this.status = SharedProductStatusEnum.ACTIVE;
        this.categoryId = command.getCategoryId();
        this.basePrice = command.getBasePrice();
        this.weight = command.getWeight();
        this.dimensions = command.getDimensions();
        this.version = 0L;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Applies an update command to this product.
     */
    public void apply(UpdateProductCommand command) {
        setName(command.getName());
        setDescription(command.getDescription());
        setShortDescription(command.getShortDescription());
        setBrand(command.getBrand());
        setModel(command.getModel());
        setCategoryId(command.getCategoryId());
        updateBasePrice(command.getBasePrice());
        updateWeight(command.getWeight());
        updateDimensions(command.getDimensions());
        updateStatus(command.getStatus());
        this.version = command.getVersion();
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getShortDescription() { return shortDescription; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public SharedProductStatusEnum getStatus() { return status; }
    public UUID getCategoryId() { return categoryId; }
    public DomainMoneyValue getBasePrice() { return basePrice; }
    public DomainWeightValue getWeight() { return weight; }
    public DomainDimensionsValue getDimensions() { return dimensions; }
    public Long getVersion() { return version; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }

    // Public setters for infrastructure mapping
    public void setId(UUID id) { this.id = id; }
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }
    public void setDescription(String description) { this.description = description; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setStatus(SharedProductStatusEnum status) { this.status = status; }
    public void setCategoryId(UUID categoryId) {
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId must not be null");
    }
    public void setBasePrice(DomainMoneyValue basePrice) { this.basePrice = basePrice; }
    public void setWeight(DomainWeightValue weight) { this.weight = weight; }
    public void setDimensions(DomainDimensionsValue dimensions) { this.dimensions = dimensions; }
    public void setVersion(Long version) { this.version = version; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public void updateStatus(SharedProductStatusEnum newStatus) {
        Objects.requireNonNull(newStatus, "newStatus must not be null");
        this.status = newStatus;
        touch();
    }

    public void updateBasePrice(DomainMoneyValue newPrice) {
        Objects.requireNonNull(newPrice, "newPrice must not be null");
        this.basePrice = newPrice;
        touch();
    }

    public void updateWeight(DomainWeightValue newWeight) {
        this.weight = newWeight;
        touch();
    }

    public void updateDimensions(DomainDimensionsValue newDimensions) {
        this.dimensions = newDimensions;
        touch();
    }

    public boolean isAvailable() {
        return SharedProductStatusEnum.ACTIVE.equals(this.status);
    }

    public String toSnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(id).append("\"");
        sb.append(",\"name\":\"").append(name).append("\"");
        sb.append(",\"description\":\"").append(description).append("\"");
        sb.append(",\"shortDescription\":\"").append(shortDescription).append("\"");
        sb.append(",\"brand\":\"").append(brand).append("\"");
        sb.append(",\"model\":\"").append(model).append("\"");
        sb.append(",\"status\":\"").append(status).append("\"");
        sb.append(",\"categoryId\":\"").append(categoryId).append("\"");
        sb.append(",\"basePrice\":{");
        sb.append("\"amount\":").append(basePrice.getAmount()).append(",");
        sb.append("\"currency\":\"").append(basePrice.getCurrency()).append("\"}");
        if (weight != null) {
            sb.append(",\"weight\":{");
            sb.append("\"value\":").append(weight.getValue()).append(",");
            sb.append("\"unit\":\"").append(weight.getUnit()).append("\"}");
        }
        if (dimensions != null) {
            sb.append(",\"dimensions\":{");
            sb.append("\"length\":").append(dimensions.getLength()).append(",");
            sb.append("\"width\":").append(dimensions.getWidth()).append(",");
            sb.append("\"height\":").append(dimensions.getHeight()).append(",");
            sb.append("\"unit\":\"").append(dimensions.getUnit()).append("\"}");
        }
        sb.append(",\"version\":").append(version);
        sb.append(",\"createdAt\":\"").append(createdAt).append("\"");
        sb.append(",\"updatedAt\":\"").append(updatedAt).append("\"");
        sb.append("}");
        return sb.toString();
    }

    // Internal methods
    private void touch() {
        this.updatedAt = ZonedDateTime.now();
        this.version++;
    }
}