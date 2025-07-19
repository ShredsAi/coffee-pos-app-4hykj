package ai.shreds.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ai.shreds.domain.entities.DomainProductVariantEntity;

/**
 * Output port for persisting and retrieving ProductVariant entities.
 */
public interface DomainOutputPortProductVariantRepository {
    DomainProductVariantEntity save(DomainProductVariantEntity variant);
    Optional<DomainProductVariantEntity> findById(UUID id);
    List<DomainProductVariantEntity> findByProductId(UUID productId);
    boolean existsByProductIdAndTypeAndValue(UUID productId, String type, String value);
    void deleteByProductId(UUID productId);
}