package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainProductVariantEntity;
import ai.shreds.domain.ports.DomainOutputPortProductVariantRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the Product Variant Repository port.
 * Handles persistence operations for product variants using JPA.
 */
@Repository
public class InfrastructureProductVariantRepositoryImpl implements DomainOutputPortProductVariantRepository {

    private final InfrastructureProductVariantJpaRepository variantJpaRepository;
    private final InfrastructureVariantMapper variantMapper;

    public InfrastructureProductVariantRepositoryImpl(
            InfrastructureProductVariantJpaRepository variantJpaRepository,
            InfrastructureVariantMapper variantMapper) {
        this.variantJpaRepository = variantJpaRepository;
        this.variantMapper = variantMapper;
    }

    @Override
    public DomainProductVariantEntity save(DomainProductVariantEntity variant) {
        InfrastructureProductVariantJpaEntity jpaEntity = variantMapper.toJpaEntity(variant);
        jpaEntity = variantJpaRepository.save(jpaEntity);
        return variantMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public Optional<DomainProductVariantEntity> findById(UUID id) {
        return variantJpaRepository.findById(id)
                .map(variantMapper::toDomainEntity);
    }

    @Override
    public List<DomainProductVariantEntity> findByProductId(UUID productId) {
        List<InfrastructureProductVariantJpaEntity> jpaEntities = 
                variantJpaRepository.findByProductIdOrderByVariantTypeAscDisplayOrderAsc(productId);
        return variantMapper.toDomainEntities(jpaEntities);
    }

    @Override
    public boolean existsByProductIdAndTypeAndValue(UUID productId, String type, String value) {
        return variantJpaRepository.existsByProductIdAndVariantTypeAndVariantValue(productId, type, value);
    }

    @Override
    @Transactional
    public void deleteByProductId(UUID productId) {
        variantJpaRepository.deleteByProductId(productId);
    }
}