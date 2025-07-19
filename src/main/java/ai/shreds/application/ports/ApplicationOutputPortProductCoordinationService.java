package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedCoordinationContextDTO;
import ai.shreds.application.services.ApplicationCoordinationResultDTO;

import java.util.UUID;

public interface ApplicationOutputPortProductCoordinationService {
    ApplicationCoordinationResultDTO updateProduct(UUID productId, SharedCoordinationContextDTO context);
}
