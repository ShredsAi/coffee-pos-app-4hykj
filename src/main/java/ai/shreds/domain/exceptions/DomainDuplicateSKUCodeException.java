package ai.shreds.domain.exceptions;

/**
 * Exception thrown when attempting to create a SKU with a code that already exists.
 */
public class DomainDuplicateSKUCodeException extends RuntimeException {
    public DomainDuplicateSKUCodeException(String code) {
        super("Duplicate SKU code: " + code);
    }
}