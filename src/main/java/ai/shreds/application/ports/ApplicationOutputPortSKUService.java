package ai.shreds.application.ports;

import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.value_objects.CreateSkuCommand;

public interface ApplicationOutputPortSKUService {
    DomainSKUEntity createSku(CreateSkuCommand command);
}
