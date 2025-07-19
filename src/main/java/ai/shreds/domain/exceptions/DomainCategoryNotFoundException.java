package ai.shreds.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a category with the specified ID is not found.
 */
public class DomainCategoryNotFoundException extends RuntimeException {
    public DomainCategoryNotFoundException(UUID categoryId) {
        super("Category not found with id: " + categoryId);
    }
}