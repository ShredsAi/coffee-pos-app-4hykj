package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.ports.DomainOutputPortSKURepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the SKU Repository port.
 * Handles persistence operations for SKUs using JPA.
 */
@Repository
public class InfrastructureSKURepositoryImpl implements DomainOutputPortSKURepository {

    private final InfrastructureSKUJpaRepository skuJpaRepository;
    private final InfrastructureSKUMapper skuMapper;
    private final ObjectMapper objectMapper;

    public InfrastructureSKURepositoryImpl(
            InfrastructureSKUJpaRepository skuJpaRepository,
            InfrastructureSKUMapper skuMapper,
            ObjectMapper objectMapper) {
        this.skuJpaRepository = skuJpaRepository;
        this.skuMapper = skuMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public DomainSKUEntity save(DomainSKUEntity sku) {
        InfrastructureSKUJpaEntity jpaEntity = skuMapper.toJpaEntity(sku);
        jpaEntity = skuJpaRepository.save(jpaEntity);
        return skuMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public Optional<DomainSKUEntity> findById(UUID id) {
        return skuJpaRepository.findById(id)
                .map(skuMapper::toDomainEntity);
    }

    @Override
    public List<DomainSKUEntity> findByProductId(UUID productId) {
        List<InfrastructureSKUJpaEntity> jpaEntities = skuJpaRepository.findByProductId(productId);
        return skuMapper.toDomainEntities(jpaEntities);
    }

    @Override
    public boolean existsByCode(String code) {
        return skuJpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByProductIdAndVariantCombination(UUID productId, Map<String, String> variantCombination) {
        if (variantCombination == null || variantCombination.isEmpty()) {
            return false;
        }
        
        try {
            String jsonVariantCombination = objectMapper.writeValueAsString(variantCombination);
            return skuJpaRepository.existsByProductIdAndVariantCombination(productId, jsonVariantCombination);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize variant combination for query", e);
        }
    }

    @Override
    @Transactional
    public void deleteByProductId(UUID productId) {
        skuJpaRepository.deleteByProductId(productId);
    }
}