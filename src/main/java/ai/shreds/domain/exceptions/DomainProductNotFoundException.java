package ai.shreds.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a product with the specified ID is not found.
 */
public class DomainProductNotFoundException extends RuntimeException {
    public DomainProductNotFoundException(UUID productId) {
        super("Product not found with id: " + productId);
    }
}