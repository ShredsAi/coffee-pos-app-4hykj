package ai.shreds.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a category hierarchy cycle is detected.
 */
public class DomainCategoryHierarchyCycleException extends RuntimeException {
    public DomainCategoryHierarchyCycleException(UUID categoryId, UUID parentId) {
        super("Category hierarchy cycle detected: category " + categoryId + " cannot be child of " + parentId);
    }
}