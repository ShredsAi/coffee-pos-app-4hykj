package ai.shreds.application.services;

import ai.shreds.shared.dtos.*;
import ai.shreds.shared.value_objects.*;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationProductMapper {

    public CreateProductCommand toCreateProductCommand(SharedCreateProductRequestDTO dto) {
        log.debug("Mapping SharedCreateProductRequestDTO to CreateProductCommand");
        
        return CreateProductCommand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .shortDescription(dto.getShortDescription())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .categoryId(dto.getCategoryId())
                .basePrice(mapSharedMoneyValueToDomain(dto.getBasePrice()))
                .weight(dto.getWeight() != null ? mapSharedWeightValueToDomain(dto.getWeight()) : null)
                .dimensions(dto.getDimensions() != null ? mapSharedDimensionsValueToDomain(dto.getDimensions()) : null)
                .build();
    }

    public UpdateProductCommand toUpdateProductCommand(SharedUpdateProductRequestDTO dto) {
        log.debug("Mapping SharedUpdateProductRequestDTO to UpdateProductCommand");
        
        return UpdateProductCommand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .shortDescription(dto.getShortDescription())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .categoryId(dto.getCategoryId())
                .basePrice(mapSharedMoneyValueToDomain(dto.getBasePrice()))
                .weight(dto.getWeight() != null ? mapSharedWeightValueToDomain(dto.getWeight()) : null)
                .dimensions(dto.getDimensions() != null ? mapSharedDimensionsValueToDomain(dto.getDimensions()) : null)
                .status(dto.getStatus())
                .version(dto.getVersion())
                .build();
    }

    public SharedProductResponseDTO toProductResponseDTO(DomainProductEntity product) {
        log.debug("Mapping DomainProductEntity to SharedProductResponseDTO");
        
        return SharedProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .brand(product.getBrand())
                .model(product.getModel())
                .status(product.getStatus())
                .categoryId(product.getCategoryId())
                .basePrice(mapDomainMoneyValueToShared(product.getBasePrice()))
                .weight(product.getWeight() != null ? mapDomainWeightValueToShared(product.getWeight()) : null)
                .dimensions(product.getDimensions() != null ? mapDomainDimensionsValueToShared(product.getDimensions()) : null)
                .version(product.getVersion())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public SharedPagedProductResponseDTO toPagedProductResponseDTO(Page<DomainProductEntity> page) {
        log.debug("Mapping Page<DomainProductEntity> to SharedPagedProductResponseDTO");
        
        List<SharedProductResponseDTO> content = page.getContent().stream()
                .map(this::toProductResponseDTO)
                .collect(Collectors.toList());

        return SharedPagedProductResponseDTO.builder()
                .content(content)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public SharedProductSearchCriteriaDTO toSearchCriteria(SharedProductSearchParams params) {
        log.debug("Mapping SharedProductSearchParams to SharedProductSearchCriteriaDTO");
        
        return SharedProductSearchCriteriaDTO.builder()
                .categoryId(params.getCategoryId())
                .status(params.getStatus())
                .search(params.getSearch())
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