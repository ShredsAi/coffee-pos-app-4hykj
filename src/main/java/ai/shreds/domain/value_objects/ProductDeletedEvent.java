package ai.shreds.domain.value_objects;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Event emitted when a product is deleted.
 */
public final class ProductDeletedEvent {
    private final UUID eventId;
    private final UUID productId;
    private final ZonedDateTime occurredAt;

    public ProductDeletedEvent(UUID eventId, UUID productId, ZonedDateTime occurredAt) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getProductId() {
        return productId;
    }

    public ZonedDateTime getOccurredAt() {
        return occurredAt;
    }
}