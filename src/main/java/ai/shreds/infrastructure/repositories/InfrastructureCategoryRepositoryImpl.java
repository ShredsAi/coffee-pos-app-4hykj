package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.ports.DomainOutputPortCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the Category Repository port.
 * Handles persistence operations for categories using JPA.
 */
@Repository
public class InfrastructureCategoryRepositoryImpl implements DomainOutputPortCategoryRepository {

    private final InfrastructureCategoryJpaRepository categoryJpaRepository;
    private final InfrastructureProductJpaRepository productJpaRepository;
    private final InfrastructureCategoryMapper categoryMapper;

    public InfrastructureCategoryRepositoryImpl(
            InfrastructureCategoryJpaRepository categoryJpaRepository,
            InfrastructureProductJpaRepository productJpaRepository,
            InfrastructureCategoryMapper categoryMapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public DomainCategoryEntity save(DomainCategoryEntity category) {
        InfrastructureCategoryJpaEntity jpaEntity = categoryMapper.toJpaEntity(category);
        jpaEntity = categoryJpaRepository.save(jpaEntity);
        return categoryMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public Optional<DomainCategoryEntity> findById(UUID id) {
        return categoryJpaRepository.findById(id)
                .map(categoryMapper::toDomainEntity);
    }

    @Override
    public List<DomainCategoryEntity> findAll() {
        List<InfrastructureCategoryJpaEntity> jpaEntities = categoryJpaRepository.findAllByOrderByLevelAscPathAsc();
        return categoryMapper.toDomainEntities(jpaEntities);
    }

    @Override
    public List<DomainCategoryEntity> findByParentCategoryId(UUID parentId) {
        List<InfrastructureCategoryJpaEntity> jpaEntities = 
                categoryJpaRepository.findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId);
        return categoryMapper.toDomainEntities(jpaEntities);
    }

    @Override
    public boolean existsByParentCategoryIdAndName(UUID parentId, String name) {
        return categoryJpaRepository.existsByParentCategoryIdAndName(parentId, name);
    }

    @Override
    public boolean hasProducts(UUID categoryId) {
        List<InfrastructureProductJpaEntity> products = productJpaRepository.findByCategoryId(categoryId);
        return !products.isEmpty();
    }
}