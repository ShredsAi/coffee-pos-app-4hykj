package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedCreateCategoryRequestDTO;
import ai.shreds.shared.dtos.SharedCategoryResponseDTO;
import ai.shreds.shared.dtos.SharedCategoryTreeResponseDTO;

public interface ApplicationInputPortCategoryService {
    SharedCategoryResponseDTO createCategory(SharedCreateCategoryRequestDTO request);
    SharedCategoryTreeResponseDTO getCategoryTree();
}
