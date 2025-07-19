package ai.shreds.domain.exceptions;

import ai.shreds.shared.utils.SharedProductStatusEnum;

/**
 * Exception thrown when a requested status transition is not allowed.
 */
public class DomainInvalidStatusTransitionException extends RuntimeException {
    public DomainInvalidStatusTransitionException(SharedProductStatusEnum from, SharedProductStatusEnum to) {
        super("Invalid status transition from " + from + " to " + to);
    }
}