package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainCategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InfrastructureCategoryMapper {
    public DomainCategoryEntity toDomainEntity(InfrastructureCategoryJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        DomainCategoryEntity domain = new DomainCategoryEntity();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setParentCategoryId(entity.getParentCategoryId());
        domain.setPath(entity.getPath());
        domain.setLevel(entity.getLevel());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public InfrastructureCategoryJpaEntity toJpaEntity(DomainCategoryEntity domain) {
        if (domain == null) {
            return null;
        }
        InfrastructureCategoryJpaEntity entity = new InfrastructureCategoryJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setParentCategoryId(domain.getParentCategoryId());
        entity.setPath(domain.getPath());
        entity.setLevel(domain.getLevel());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public List<DomainCategoryEntity> toDomainEntities(List<InfrastructureCategoryJpaEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }
}
