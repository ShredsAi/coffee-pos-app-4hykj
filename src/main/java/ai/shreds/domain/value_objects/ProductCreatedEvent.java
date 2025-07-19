package ai.shreds.domain.value_objects;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import ai.shreds.domain.value_objects.DomainMoneyValue;

/**
 * Event emitted when a product is created.
 */
public final class ProductCreatedEvent {
    private final UUID eventId;
    private final UUID productId;
    private final String name;
    private final UUID categoryId;
    private final DomainMoneyValue basePrice;
    private final ZonedDateTime occurredAt;

    public ProductCreatedEvent(UUID eventId,
                               UUID productId,
                               String name,
                               UUID categoryId,
                               DomainMoneyValue basePrice,
                               ZonedDateTime occurredAt) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId must not be null");
        this.basePrice = Objects.requireNonNull(basePrice, "basePrice must not be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public DomainMoneyValue getBasePrice() {
        return basePrice;
    }

    public ZonedDateTime getOccurredAt() {
        return occurredAt;
    }
}