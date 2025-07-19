package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortSKUService;
import ai.shreds.application.ports.ApplicationOutputPortSKUService;
import ai.shreds.application.ports.ApplicationOutputPortProductCommandService;
import ai.shreds.application.ports.ApplicationOutputPortEventPublisher;
import ai.shreds.shared.dtos.*;
import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.CreateSkuCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationSKUService implements ApplicationInputPortSKUService {

    private final ApplicationOutputPortSKUService skuService;
    private final ApplicationOutputPortProductCommandService productCommandService;
    private final ApplicationOutputPortEventPublisher eventPublisher;
    private final ApplicationSKUMapper skuMapper;

    @Override
    public SharedSKUResponseDTO createSKU(SharedCreateSKURequestDTO request) {
        log.debug("Creating SKU for product ID: {}", request.getProductId());
        
        // Validate that parent product exists
        DomainProductEntity parentProduct = productCommandService.findProduct(request.getProductId());
        log.debug("Parent product found: {}", parentProduct.getName());
        
        // Map request DTO to command
        CreateSkuCommand command = skuMapper.toCreateSkuCommand(request);
        
        // Execute business logic through domain service
        DomainSKUEntity createdSKU = skuService.createSku(command);
        
        // Map domain entity to response DTO
        SharedSKUResponseDTO response = skuMapper.toSkuResponseDTO(createdSKU);
        
        // Publish domain event
        SharedProductUpdatedEventDTO event = SharedProductUpdatedEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("PRODUCT_UPDATED")
                .productId(request.getProductId())
                .changeType("SKU_ADDED")
                .newValues(java.util.Map.of(
                    "skuId", createdSKU.getId().toString(),
                    "skuCode", createdSKU.getCode(),
                    "variantCombination", createdSKU.getVariantCombination() != null ? createdSKU.getVariantCombination() : java.util.Map.of()
                ))
                .timestamp(ZonedDateTime.now())
                .build();
        eventPublisher.publishEvent(event);
        
        log.info("SKU created successfully with ID: {} and code: {}", createdSKU.getId(), createdSKU.getCode());
        return response;
    }
}