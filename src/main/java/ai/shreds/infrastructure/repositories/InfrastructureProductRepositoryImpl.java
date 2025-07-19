package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.ports.DomainOutputPortProductRepository;
import ai.shreds.shared.dtos.SharedProductSearchCriteriaDTO;
import ai.shreds.shared.utils.SharedProductStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the Product Repository port.
 * Handles persistence operations for products using JPA.
 */
@Repository
public class InfrastructureProductRepositoryImpl implements DomainOutputPortProductRepository {

    private final InfrastructureProductJpaRepository productJpaRepository;
    private final InfrastructureProductMapper productMapper;

    public InfrastructureProductRepositoryImpl(
            InfrastructureProductJpaRepository productJpaRepository,
            InfrastructureProductMapper productMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productMapper = productMapper;
    }

    @Override
    public DomainProductEntity save(DomainProductEntity product) {
        InfrastructureProductJpaEntity jpaEntity = productMapper.toJpaEntity(product);
        jpaEntity = productJpaRepository.save(jpaEntity);
        return productMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public Optional<DomainProductEntity> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(productMapper::toDomainEntity);
    }

    @Override
    public Page<DomainProductEntity> findAll(SharedProductSearchCriteriaDTO criteria, Pageable pageable) {
        Specification<InfrastructureProductJpaEntity> spec = buildSpecification(criteria);
        Page<InfrastructureProductJpaEntity> jpaPage = productJpaRepository.findAll(spec, pageable);
        
        List<DomainProductEntity> content = productMapper.toDomainEntities(jpaPage.getContent());
        return new PageImpl<>(content, pageable, jpaPage.getTotalElements());
    }

    @Override
    public boolean existsByNameAndCategoryId(String name, UUID categoryId) {
        return productJpaRepository.existsByNameAndCategoryId(name, categoryId);
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }

    /**
     * Builds a JPA Specification for dynamic querying based on search criteria.
     *
     * @param criteria The search criteria to build predicates from
     * @return A specification for JPA queries
     */
    private Specification<InfrastructureProductJpaEntity> buildSpecification(SharedProductSearchCriteriaDTO criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add category filter
            if (criteria.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), criteria.getCategoryId()));
            }

            // Add status filter
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus().name()));
            }

            // Add text search filter (search in name, description, brand, model)
            if (StringUtils.hasText(criteria.getSearch())) {
                String searchPattern = "%" + criteria.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern),
                    cb.like(cb.lower(root.get("shortDescription")), searchPattern),
                    cb.like(cb.lower(root.get("brand")), searchPattern),
                    cb.like(cb.lower(root.get("model")), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
