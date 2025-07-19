package ai.shreds.application.services;

import ai.shreds.shared.dtos.*;
import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.value_objects.CreateCategoryCommand;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationCategoryMapper {

    public CreateCategoryCommand toCreateCategoryCommand(SharedCreateCategoryRequestDTO dto) {
        log.debug("Mapping SharedCreateCategoryRequestDTO to CreateCategoryCommand");
        
        return CreateCategoryCommand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .parentCategoryId(dto.getParentCategoryId())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }

    public SharedCategoryResponseDTO toCategoryResponseDTO(DomainCategoryEntity category) {
        log.debug("Mapping DomainCategoryEntity to SharedCategoryResponseDTO");
        
        return SharedCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentCategoryId(category.getParentCategoryId())
                .path(category.getPath())
                .level(category.getLevel())
                .displayOrder(category.getDisplayOrder())
                .isActive(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public SharedCategoryTreeResponseDTO toCategoryTreeResponseDTO(List<DomainCategoryEntity> categories) {
        log.debug("Building category tree from {} categories", categories.size());
        
        // Filter only active categories
        List<DomainCategoryEntity> activeCategories = categories.stream()
                .filter(DomainCategoryEntity::isActive)
                .collect(Collectors.toList());
        
        // Create a root node for the tree
        SharedCategoryTreeResponseDTO rootNode = SharedCategoryTreeResponseDTO.builder()
                .id(null) // Root node doesn't have an ID
                .name("Root")
                .description("Root category")
                .path("/")
                .level(0)
                .displayOrder(0)
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        
        // Build tree recursively
        List<SharedCategoryTreeResponseDTO> rootCategories = buildCategoryTree(activeCategories, null);
        rootNode.setChildren(rootCategories);
        
        return rootNode;
    }

    private List<SharedCategoryTreeResponseDTO> buildCategoryTree(List<DomainCategoryEntity> categories, UUID parentId) {
        return categories.stream()
                .filter(category -> (parentId == null && category.getParentCategoryId() == null) || 
                        (parentId != null && parentId.equals(category.getParentCategoryId())))
                .map(category -> {
                    SharedCategoryTreeResponseDTO node = SharedCategoryTreeResponseDTO.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .description(category.getDescription())
                            .path(category.getPath())
                            .level(category.getLevel())
                            .displayOrder(category.getDisplayOrder())
                            .isActive(category.isActive())
                            .children(buildCategoryTree(categories, category.getId()))
                            .build();
                    return node;
                })
                .sorted((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()))
                .collect(Collectors.toList());
    }
}