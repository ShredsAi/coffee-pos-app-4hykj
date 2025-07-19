package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.DomainDimensionsValue;
import ai.shreds.domain.value_objects.DomainMoneyValue;
import ai.shreds.domain.value_objects.DomainWeightValue;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InfrastructureProductMapper {
    public DomainProductEntity toDomainEntity(InfrastructureProductJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        DomainMoneyValue money = new DomainMoneyValue(entity.getBasePriceAmount(), entity.getBasePriceCurrency());
        DomainWeightValue weight = entity.getWeightValue() != null
                ? new DomainWeightValue(entity.getWeightValue(), entity.getWeightUnit())
                : null;
        DomainDimensionsValue dimensions = (entity.getDimLength() != null && entity.getDimWidth() != null && entity.getDimHeight() != null)
                ? new DomainDimensionsValue(entity.getDimLength(), entity.getDimWidth(), entity.getDimHeight(), entity.getDimUnit())
                : null;
        DomainProductEntity domain = new DomainProductEntity();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setShortDescription(entity.getShortDescription());
        domain.setBrand(entity.getBrand());
        domain.setModel(entity.getModel());
        domain.setStatus(SharedProductStatusEnum.valueOf(entity.getStatus()));
        domain.setCategoryId(entity.getCategoryId());
        domain.setBasePrice(money);
        domain.setWeight(weight);
        domain.setDimensions(dimensions);
        domain.setVersion(entity.getVersion());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public InfrastructureProductJpaEntity toJpaEntity(DomainProductEntity domain) {
        if (domain == null) {
            return null;
        }
        InfrastructureProductJpaEntity entity = new InfrastructureProductJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setShortDescription(domain.getShortDescription());
        entity.setBrand(domain.getBrand());
        entity.setModel(domain.getModel());
        entity.setStatus(domain.getStatus().name());
        entity.setCategoryId(domain.getCategoryId());
        entity.setBasePriceAmount(domain.getBasePrice().getAmount());
        entity.setBasePriceCurrency(domain.getBasePrice().getCurrency());
        if (domain.getWeight() != null) {
            entity.setWeightValue(domain.getWeight().getValue());
            entity.setWeightUnit(domain.getWeight().getUnit());
        }
        if (domain.getDimensions() != null) {
            entity.setDimLength(domain.getDimensions().getLength());
            entity.setDimWidth(domain.getDimensions().getWidth());
            entity.setDimHeight(domain.getDimensions().getHeight());
            entity.setDimUnit(domain.getDimensions().getUnit());
        }
        entity.setVersion(domain.getVersion());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public List<DomainProductEntity> toDomainEntities(List<InfrastructureProductJpaEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }
}
