package ai.shreds.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ai.shreds.domain.entities.DomainCategoryEntity;

/**
 * Output port for persisting and retrieving Category entities.
 */
public interface DomainOutputPortCategoryRepository {
    DomainCategoryEntity save(DomainCategoryEntity category);
    Optional<DomainCategoryEntity> findById(UUID id);
    List<DomainCategoryEntity> findAll();
    List<DomainCategoryEntity> findByParentCategoryId(UUID parentId);
    boolean existsByParentCategoryIdAndName(UUID parentId, String name);
    boolean hasProducts(UUID categoryId);
}