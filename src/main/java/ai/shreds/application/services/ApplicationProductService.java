package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortProductService;
import ai.shreds.application.ports.ApplicationOutputPortProductCommandService;
import ai.shreds.application.ports.ApplicationOutputPortEventPublisher;
import ai.shreds.shared.dtos.*;
import ai.shreds.shared.value_objects.SharedProductSearchParams;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.CreateProductCommand;
import ai.shreds.domain.value_objects.UpdateProductCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ApplicationProductService implements ApplicationInputPortProductService {

    private final ApplicationOutputPortProductCommandService productCommandService;
    private final ApplicationOutputPortEventPublisher eventPublisher;
    private final ApplicationProductMapper productMapper;

    @Override
    public SharedProductResponseDTO createProduct(SharedCreateProductRequestDTO request) {
        log.debug("Creating product with name: {}", request.getName());
        
        // Map request DTO to command
        CreateProductCommand command = productMapper.toCreateProductCommand(request);
        
        // Execute business logic through domain service
        DomainProductEntity createdProduct = productCommandService.createProduct(command);
        
        // Map domain entity to response DTO
        SharedProductResponseDTO response = productMapper.toProductResponseDTO(createdProduct);
        
        // Publish domain event
        SharedProductUpdatedEventDTO event = SharedProductUpdatedEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("PRODUCT_CREATED")
                .productId(createdProduct.getId())
                .changeType("CREATED")
                .newValues(java.util.Map.of(
                    "name", createdProduct.getName(),
                    "status", createdProduct.getStatus().toString(),
                    "categoryId", createdProduct.getCategoryId().toString()
                ))
                .timestamp(ZonedDateTime.now())
                .build();
        eventPublisher.publishEvent(event);
        
        log.info("Product created successfully with ID: {}", createdProduct.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SharedProductResponseDTO getProduct(UUID id) {
        log.debug("Retrieving product with ID: {}", id);
        
        DomainProductEntity product = productCommandService.findProduct(id);
        SharedProductResponseDTO response = productMapper.toProductResponseDTO(product);
        
        log.debug("Product retrieved successfully: {}", product.getName());
        return response;
    }

    @Override
    public SharedProductResponseDTO updateProduct(UUID id, SharedUpdateProductRequestDTO request) {
        log.debug("Updating product with ID: {}", id);
        
        // Map request DTO to command
        UpdateProductCommand command = productMapper.toUpdateProductCommand(request);
        
        // Capture old values for event
        DomainProductEntity existingProduct = productCommandService.findProduct(id);
        java.util.Map<String, Object> oldValues = java.util.Map.of(
            "name", existingProduct.getName(),
            "status", existingProduct.getStatus().toString(),
            "basePrice", java.util.Map.of(
                "amount", existingProduct.getBasePrice().getAmount(),
                "currency", existingProduct.getBasePrice().getCurrency()
            )
        );
        
        // Execute business logic through domain service
        DomainProductEntity updatedProduct = productCommandService.updateProduct(id, command);
        
        // Map domain entity to response DTO
        SharedProductResponseDTO response = productMapper.toProductResponseDTO(updatedProduct);
        
        // Publish domain event
        java.util.Map<String, Object> newValues = java.util.Map.of(
            "name", updatedProduct.getName(),
            "status", updatedProduct.getStatus().toString(),
            "basePrice", java.util.Map.of(
                "amount", updatedProduct.getBasePrice().getAmount(),
                "currency", updatedProduct.getBasePrice().getCurrency()
            )
        );
        
        SharedProductUpdatedEventDTO event = SharedProductUpdatedEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("PRODUCT_UPDATED")
                .productId(updatedProduct.getId())
                .changeType("UPDATED")
                .oldValues(oldValues)
                .newValues(newValues)
                .timestamp(ZonedDateTime.now())
                .build();
        eventPublisher.publishEvent(event);
        
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return response;
    }

    @Override
    public void deleteProduct(UUID id) {
        log.debug("Deleting product with ID: {}", id);
        
        // Capture product info before deletion for event
        DomainProductEntity productToDelete = productCommandService.findProduct(id);
        
        // Execute business logic through domain service
        productCommandService.deleteProduct(id);
        
        // Publish domain event
        SharedProductUpdatedEventDTO event = SharedProductUpdatedEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("PRODUCT_DELETED")
                .productId(productToDelete.getId())
                .changeType("DELETED")
                .oldValues(java.util.Map.of(
                    "name", productToDelete.getName(),
                    "status", productToDelete.getStatus().toString()
                ))
                .timestamp(ZonedDateTime.now())
                .build();
        eventPublisher.publishEvent(event);
        
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public SharedPagedProductResponseDTO listProducts(SharedProductSearchParams searchParams, Pageable pageable) {
        log.debug("Listing products with search params: {}, page: {}", searchParams, pageable);
        
        // Map search params to criteria
        SharedProductSearchCriteriaDTO criteria = productMapper.toSearchCriteria(searchParams);
        
        // Execute search through domain service
        Page<DomainProductEntity> productPage = productCommandService.searchProducts(criteria, pageable);
        
        // Map page to response DTO
        SharedPagedProductResponseDTO response = productMapper.toPagedProductResponseDTO(productPage);
        
        log.debug("Retrieved {} products out of {} total", productPage.getContent().size(), productPage.getTotalElements());
        return response;
    }
}