package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedCreateSKURequestDTO;
import ai.shreds.shared.dtos.SharedSKUResponseDTO;

public interface ApplicationInputPortSKUService {
    SharedSKUResponseDTO createSKU(SharedCreateSKURequestDTO request);
}
