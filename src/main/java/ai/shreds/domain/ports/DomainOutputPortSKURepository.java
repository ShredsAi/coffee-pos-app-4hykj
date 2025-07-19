package ai.shreds.domain.ports;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import ai.shreds.domain.entities.DomainSKUEntity;

/**
 * Output port for persisting and retrieving SKU entities.
 */
public interface DomainOutputPortSKURepository {

    DomainSKUEntity save(DomainSKUEntity sku);

    Optional<DomainSKUEntity> findById(UUID id);

    List<DomainSKUEntity> findByProductId(UUID productId);

    boolean existsByCode(String code);

    boolean existsByProductIdAndVariantCombination(UUID productId, Map<String, String> variantCombination);

    void deleteByProductId(UUID productId);
}