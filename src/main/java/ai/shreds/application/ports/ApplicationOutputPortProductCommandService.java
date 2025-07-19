package ai.shreds.application.ports;

import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.CreateProductCommand;
import ai.shreds.domain.value_objects.UpdateProductCommand;
import ai.shreds.shared.dtos.SharedProductSearchCriteriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ApplicationOutputPortProductCommandService {
    DomainProductEntity createProduct(CreateProductCommand command);
    DomainProductEntity updateProduct(UUID productId, UpdateProductCommand command);
    void deleteProduct(UUID productId);
    DomainProductEntity findProduct(UUID productId);
    Page<DomainProductEntity> searchProducts(SharedProductSearchCriteriaDTO criteria, Pageable pageable);
}
