package ai.shreds.application.ports;

import ai.shreds.shared.dtos.SharedCreateProductRequestDTO;
import ai.shreds.shared.dtos.SharedUpdateProductRequestDTO;
import ai.shreds.shared.dtos.SharedProductResponseDTO;
import ai.shreds.shared.dtos.SharedPagedProductResponseDTO;
import ai.shreds.shared.value_objects.SharedProductSearchParams;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationInputPortProductService {
    SharedProductResponseDTO createProduct(SharedCreateProductRequestDTO request);
    SharedProductResponseDTO getProduct(UUID id);
    SharedProductResponseDTO updateProduct(UUID id, SharedUpdateProductRequestDTO request);
    void deleteProduct(UUID id);
    SharedPagedProductResponseDTO listProducts(SharedProductSearchParams searchParams, Pageable pageable);
}
