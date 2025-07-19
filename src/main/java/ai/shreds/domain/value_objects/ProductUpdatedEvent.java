package ai.shreds.domain.value_objects;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import ai.shreds.domain.value_objects.DomainMoneyValue;

/**
 * Event emitted when a product is updated.
 */
public final class ProductUpdatedEvent {
    private final UUID eventId;
    private final UUID productId;
    private final String changeType;
    private final Map<String, Object> oldValues;
    private final Map<String, Object> newValues;
    private final ZonedDateTime occurredAt;

    public ProductUpdatedEvent(UUID eventId,
                               UUID productId,
                               String changeType,
                               Map<String, Object> oldValues,
                               Map<String, Object> newValues,
                               ZonedDateTime occurredAt) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.changeType = Objects.requireNonNull(changeType, "changeType must not be null");
        this.oldValues = Objects.requireNonNull(oldValues, "oldValues must not be null");
        this.newValues = Objects.requireNonNull(newValues, "newValues must not be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getChangeType() {
        return changeType;
    }

    public Map<String, Object> getOldValues() {
        return oldValues;
    }

    public Map<String, Object> getNewValues() {
        return newValues;
    }

    public ZonedDateTime getOccurredAt() {
        return occurredAt;
    }
}