package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainProductVariantEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InfrastructureVariantMapper {

    public DomainProductVariantEntity toDomainEntity(InfrastructureProductVariantJpaEntity entity) {
        if (entity == null) return null;
        DomainProductVariantEntity domain = new DomainProductVariantEntity();
        domain.setId(entity.getId());
        domain.setProductId(entity.getProductId());
        domain.setVariantType(entity.getVariantType());
        domain.setVariantValue(entity.getVariantValue());
        domain.setDisplayName(entity.getDisplayName());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public InfrastructureProductVariantJpaEntity toJpaEntity(DomainProductVariantEntity domain) {
        if (domain == null) return null;
        InfrastructureProductVariantJpaEntity entity = new InfrastructureProductVariantJpaEntity();
        entity.setId(domain.getId());
        entity.setProductId(domain.getProductId());
        entity.setVariantType(domain.getVariantType());
        entity.setVariantValue(domain.getVariantValue());
        entity.setDisplayName(domain.getDisplayName());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public List<DomainProductVariantEntity> toDomainEntities(List<InfrastructureProductVariantJpaEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toDomainEntity).collect(Collectors.toList());
    }
}
