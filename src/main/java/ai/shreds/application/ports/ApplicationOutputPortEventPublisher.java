package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedProductUpdatedEventDTO;

public interface ApplicationOutputPortEventPublisher {
    void publishEvent(SharedProductUpdatedEventDTO event);
}
