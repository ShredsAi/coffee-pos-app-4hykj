package ai.shreds.domain.services;

import java.util.List;
import java.util.UUID;
import ai.shreds.application.ports.ApplicationOutputPortCategoryService;
import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.exceptions.DomainCategoryNotFoundException;
import ai.shreds.domain.ports.DomainOutputPortCategoryRepository;
import ai.shreds.domain.value_objects.CreateCategoryCommand;
import org.springframework.stereotype.Service;

/**
 * Domain service for Category operations.
 */
@Service
public class DomainCategoryService implements ApplicationOutputPortCategoryService {
    private final DomainOutputPortCategoryRepository categoryRepository;
    private final DomainCategoryHierarchyValidator hierarchyValidator;

    public DomainCategoryService(
            DomainOutputPortCategoryRepository categoryRepository,
            DomainCategoryHierarchyValidator hierarchyValidator) {
        this.categoryRepository = categoryRepository;
        this.hierarchyValidator = hierarchyValidator;
    }

    @Override
    public DomainCategoryEntity createCategory(CreateCategoryCommand command) {
        UUID parentId = command.getParentCategoryId();
        // Validate parent existence and cycles
        if (parentId != null) {
            DomainCategoryEntity parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new DomainCategoryNotFoundException(parentId));
            hierarchyValidator.assertNoCycle(parentId, null);
            validateSiblingNameUniqueness(parentId, command.getName());
            DomainCategoryEntity category = new DomainCategoryEntity(command);
            category.computePath(parent.getPath());
            category.computeLevel(parent.getLevel());
            return categoryRepository.save(category);
        } else {
            validateSiblingNameUniqueness(null, command.getName());
            DomainCategoryEntity category = new DomainCategoryEntity(command);
            category.computePath("");
            category.computeLevel(-1);
            return categoryRepository.save(category);
        }
    }

    @Override
    public List<DomainCategoryEntity> getCategoryTree() {
        return categoryRepository.findAll();
    }

    private void validateSiblingNameUniqueness(UUID parentId, String name) {
        if (categoryRepository.existsByParentCategoryIdAndName(parentId, name)) {
            throw new IllegalArgumentException(
                    String.format("Category name '%s' already exists under parent %s", name, parentId));
        }
    }
}