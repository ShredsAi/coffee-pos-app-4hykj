package ai.shreds.domain.ports;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ai.shreds.shared.dtos.SharedProductSearchCriteriaDTO;
import ai.shreds.domain.entities.DomainProductEntity;

/**
 * Output port for persisting and retrieving Product entities.
 */
public interface DomainOutputPortProductRepository {

    DomainProductEntity save(DomainProductEntity product);

    Optional<DomainProductEntity> findById(UUID id);

    Page<DomainProductEntity> findAll(SharedProductSearchCriteriaDTO criteria, Pageable pageable);

    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    void deleteById(UUID id);
}