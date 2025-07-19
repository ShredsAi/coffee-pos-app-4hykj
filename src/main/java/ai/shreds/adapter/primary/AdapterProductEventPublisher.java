package ai.shreds.adapter.primary;

import ai.shreds.shared.dtos.SharedProductUpdatedEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adapter component for publishing product-related domain events.
 * Acts as a bridge between the application layer and Spring's event publishing infrastructure.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdapterProductEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes a product updated event to all registered listeners.
     * 
     * @param event The product updated event to publish
     * @throws IllegalArgumentException if event is null
     */
    public void publishProductUpdatedEvent(SharedProductUpdatedEventDTO event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        
        log.debug("Publishing product updated event: eventId={}, productId={}, changeType={}", 
                event.getEventId(), event.getProductId(), event.getChangeType());
        
        try {
            applicationEventPublisher.publishEvent(event);
            log.info("Successfully published product updated event for product: {}", event.getProductId());
        } catch (Exception ex) {
            log.error("Failed to publish product updated event for product: {}", event.getProductId(), ex);
            // Depending on requirements, we might want to rethrow or handle gracefully
            // For now, we'll rethrow to maintain transaction consistency
            throw new RuntimeException("Failed to publish product updated event", ex);
        }
    }
}
