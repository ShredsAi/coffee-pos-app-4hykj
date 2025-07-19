package ai.shreds.application.services;

import ai.shreds.shared.dtos.*;
import ai.shreds.shared.value_objects.*;
import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.value_objects.*;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationSKUMapper {

    public CreateSkuCommand toCreateSkuCommand(SharedCreateSKURequestDTO dto) {
        log.debug("Mapping SharedCreateSKURequestDTO to CreateSkuCommand");
        
        return CreateSkuCommand.builder()
                .productId(dto.getProductId())
                .variantCombination(dto.getVariantCombination())
                .price(dto.getPrice() != null ? mapSharedMoneyValueToDomain(dto.getPrice()) : null)
                .weight(dto.getWeight() != null ? mapSharedWeightValueToDomain(dto.getWeight()) : null)
                .dimensions(dto.getDimensions() != null ? mapSharedDimensionsValueToDomain(dto.getDimensions()) : null)
                .build();
    }

    public SharedSKUResponseDTO toSkuResponseDTO(DomainSKUEntity sku) {
        log.debug("Mapping DomainSKUEntity to SharedSKUResponseDTO");
        
        return SharedSKUResponseDTO.builder()
                .id(sku.getId())
                .code(sku.getCode())
                .productId(sku.getProductId())
                .variantCombination(sku.getVariantCombination())
                .price(sku.getPrice() != null ? mapDomainMoneyValueToShared(sku.getPrice()) : null)
                .weight(sku.getWeight() != null ? mapDomainWeightValueToShared(sku.getWeight()) : null)
                .dimensions(sku.getDimensions() != null ? mapDomainDimensionsValueToShared(sku.getDimensions()) : null)
                .status(sku.getStatus())
                .createdAt(sku.getCreatedAt())
                .updatedAt(sku.getUpdatedAt())
                .build();
    }

    // Helper mapping methods for value objects
    private DomainMoneyValue mapSharedMoneyValueToDomain(SharedMoneyValueDTO dto) {
        if (dto == null) return null;
        return new DomainMoneyValue(dto.getAmount(), dto.getCurrency());
    }

    private SharedMoneyValueDTO mapDomainMoneyValueToShared(DomainMoneyValue domain) {
        if (domain == null) return null;
        return SharedMoneyValueDTO.builder()
                .amount(domain.getAmount())
                .currency(domain.getCurrency())
                .build();
    }

    private DomainWeightValue mapSharedWeightValueToDomain(SharedWeightValueDTO dto) {
        if (dto == null) return null;
        return new DomainWeightValue(dto.getValue(), dto.getUnit());
    }

    private SharedWeightValueDTO mapDomainWeightValueToShared(DomainWeightValue domain) {
        if (domain == null) return null;
        return SharedWeightValueDTO.builder()
                .value(domain.getValue())
                .unit(domain.getUnit())
                .build();
    }

    private DomainDimensionsValue mapSharedDimensionsValueToDomain(SharedDimensionsValueDTO dto) {
        if (dto == null) return null;
        return new DomainDimensionsValue(dto.getLength(), dto.getWidth(), dto.getHeight(), dto.getUnit());
    }

    private SharedDimensionsValueDTO mapDomainDimensionsValueToShared(DomainDimensionsValue domain) {
        if (domain == null) return null;
        return SharedDimensionsValueDTO.builder()
                .length(domain.getLength())
                .width(domain.getWidth())
                .height(domain.getHeight())
                .unit(domain.getUnit())
                .build();
    }
}