package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortCategoryService;
import ai.shreds.application.ports.ApplicationOutputPortCategoryService;
import ai.shreds.application.ports.ApplicationOutputPortEventPublisher;
import ai.shreds.shared.dtos.*;
import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.value_objects.CreateCategoryCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationCategoryService implements ApplicationInputPortCategoryService {

    private final ApplicationOutputPortCategoryService categoryService;
    private final ApplicationCategoryMapper categoryMapper;
    private final ApplicationOutputPortEventPublisher eventPublisher;

    @Override
    public SharedCategoryResponseDTO createCategory(SharedCreateCategoryRequestDTO request) {
        log.debug("Creating category with name: {}", request.getName());
        
        // Map request DTO to command
        CreateCategoryCommand command = categoryMapper.toCreateCategoryCommand(request);
        
        // Execute business logic through domain service
        DomainCategoryEntity createdCategory = categoryService.createCategory(command);
        
        // Map domain entity to response DTO
        SharedCategoryResponseDTO response = categoryMapper.toCategoryResponseDTO(createdCategory);
        
        // Publish domain event
        SharedProductUpdatedEventDTO event = SharedProductUpdatedEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("CATEGORY_CREATED")
                .changeType("CATEGORY_HIERARCHY_CHANGED")
                .newValues(java.util.Map.of(
                    "categoryId", createdCategory.getId().toString(),
                    "name", createdCategory.getName(),
                    "path", createdCategory.getPath(),
                    "level", createdCategory.getLevel()
                ))
                .timestamp(ZonedDateTime.now())
                .build();
        eventPublisher.publishEvent(event);
        
        log.info("Category created successfully with ID: {}", createdCategory.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SharedCategoryTreeResponseDTO getCategoryTree() {
        log.debug("Retrieving category tree");
        
        // Execute business logic through domain service
        List<DomainCategoryEntity> categories = categoryService.getCategoryTree();
        
        // Map domain entity list to tree response DTO
        SharedCategoryTreeResponseDTO response = categoryMapper.toCategoryTreeResponseDTO(categories);
        
        log.debug("Category tree retrieved successfully with {} root categories", 
            response.getChildren() != null ? response.getChildren().size() : 0);
        return response;
    }
}