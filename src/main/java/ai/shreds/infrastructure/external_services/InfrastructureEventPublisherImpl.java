package ai.shreds.infrastructure.external_services;

import ai.shreds.application.ports.ApplicationOutputPortEventPublisher;
import ai.shreds.shared.dtos.SharedProductUpdatedEventDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class InfrastructureEventPublisherImpl implements ApplicationOutputPortEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public InfrastructureEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(SharedProductUpdatedEventDTO event) {
        applicationEventPublisher.publishEvent(event);
    }
}