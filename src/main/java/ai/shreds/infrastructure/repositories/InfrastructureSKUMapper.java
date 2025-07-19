package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.value_objects.DomainDimensionsValue;
import ai.shreds.domain.value_objects.DomainMoneyValue;
import ai.shreds.domain.value_objects.DomainWeightValue;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper between DomainSKUEntity and InfrastructureSKUJpaEntity.
 * Relies on Jackson ObjectMapper to handle deep copying of variantCombination map if needed.
 */
@Component
public class InfrastructureSKUMapper {

    private final ObjectMapper objectMapper;

    public InfrastructureSKUMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DomainSKUEntity toDomainEntity(InfrastructureSKUJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        DomainMoneyValue money = entity.getPriceAmount() != null
                ? new DomainMoneyValue(entity.getPriceAmount(), entity.getPriceCurrency())
                : null;
        DomainWeightValue weight = entity.getWeightValue() != null
                ? new DomainWeightValue(entity.getWeightValue(), entity.getWeightUnit())
                : null;
        DomainDimensionsValue dimensions = (entity.getDimLength() != null && entity.getDimWidth() != null && entity.getDimHeight() != null)
                ? new DomainDimensionsValue(entity.getDimLength(), entity.getDimWidth(), entity.getDimHeight(), entity.getDimUnit())
                : null;

        DomainSKUEntity domain = new DomainSKUEntity();
        domain.setId(entity.getId());
        domain.setCode(entity.getCode());
        domain.setProductId(entity.getProductId());
        domain.setVariantCombination(deepCopy(entity.getVariantCombination()));
        domain.setPrice(money);
        domain.setWeight(weight);
        domain.setDimensions(dimensions);
        domain.setStatus(SharedProductStatusEnum.valueOf(entity.getStatus()));
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public InfrastructureSKUJpaEntity toJpaEntity(DomainSKUEntity domain) {
        if (domain == null) {
            return null;
        }
        InfrastructureSKUJpaEntity entity = new InfrastructureSKUJpaEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setProductId(domain.getProductId());
        entity.setVariantCombination(deepCopy(domain.getVariantCombination()));
        if (domain.getPrice() != null) {
            entity.setPriceAmount(domain.getPrice().getAmount());
            entity.setPriceCurrency(domain.getPrice().getCurrency());
        }
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
        entity.setStatus(domain.getStatus().name());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public List<DomainSKUEntity> toDomainEntities(List<InfrastructureSKUJpaEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDomainEntity).collect(Collectors.toList());
    }

    /**
     * Deep copies a variant combination map via JSON serialization to avoid shared mutability between layers.
     */
    private Map<String, String> deepCopy(Map<String, String> original) {
        if (original == null) {
            return Collections.emptyMap();
        }
        try {
            // Serialize then deserialize to ensure deep copy.
            String json = objectMapper.writeValueAsString(original);
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to copy variantCombination map", e);
        }
    }
}
