package ai.shreds.infrastructure.external_services;

import ai.shreds.domain.value_objects.ProductCreatedEvent;
import ai.shreds.domain.value_objects.ProductUpdatedEvent;
import ai.shreds.domain.value_objects.ProductDeletedEvent;
import ai.shreds.shared.dtos.SharedProductUpdatedEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.Collections;
import java.util.Map;

@Component
public class InfrastructureTransactionalEventListener {

    private final InfrastructureEventPublisherImpl eventPublisher;

    @Autowired
    public InfrastructureTransactionalEventListener(InfrastructureEventPublisherImpl eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        SharedProductUpdatedEventDTO dto = convertToSharedEvent(event);
        eventPublisher.publishEvent(dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductUpdatedEvent(ProductUpdatedEvent event) {
        SharedProductUpdatedEventDTO dto = convertToSharedEvent(event);
        eventPublisher.publishEvent(dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeletedEvent(ProductDeletedEvent event) {
        SharedProductUpdatedEventDTO dto = convertToSharedEvent(event);
        eventPublisher.publishEvent(dto);
    }

    private SharedProductUpdatedEventDTO convertToSharedEvent(Object domainEvent) {
        SharedProductUpdatedEventDTO shared = new SharedProductUpdatedEventDTO();
        if (domainEvent instanceof ProductCreatedEvent created) {
            shared.setEventId(created.getEventId());
            shared.setEventType("PRODUCT_CREATED");
            shared.setProductId(created.getProductId());
            shared.setChangeType("CREATED");
            shared.setOldValues(Collections.emptyMap());
            shared.setNewValues(Map.of(
                "productId", created.getProductId()
            ));
            shared.setTimestamp(created.getOccurredAt());
        } else if (domainEvent instanceof ProductUpdatedEvent updated) {
            shared.setEventId(updated.getEventId());
            shared.setEventType("PRODUCT_UPDATED");
            shared.setProductId(updated.getProductId());
            shared.setChangeType(updated.getChangeType());
            shared.setOldValues(updated.getOldValues());
            shared.setNewValues(updated.getNewValues());
            shared.setTimestamp(updated.getOccurredAt());
        } else if (domainEvent instanceof ProductDeletedEvent deleted) {
            shared.setEventId(deleted.getEventId());
            shared.setEventType("PRODUCT_DELETED");
            shared.setProductId(deleted.getProductId());
            shared.setChangeType("DELETED");
            shared.setOldValues(Collections.emptyMap());
            shared.setNewValues(Collections.emptyMap());
            shared.setTimestamp(deleted.getOccurredAt());
        }
        return shared;
    }
}