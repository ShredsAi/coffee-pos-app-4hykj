package ai.shreds.adapter.exceptions;

import ai.shreds.application.exceptions.ApplicationValidationException;
import ai.shreds.domain.exceptions.*;
import ai.shreds.shared.dtos.SharedProblemDetailsDTO;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

/**
 * Global exception handler for REST controllers.
 * Converts internal exceptions into RFC7807 Problem Details responses.
 */
@ControllerAdvice
@Slf4j
public class AdapterGlobalExceptionHandler {

    /* ========================= Validation ========================= */
    @ExceptionHandler({MethodArgumentNotValidException.class, ApplicationValidationException.class})
    public ResponseEntity<SharedProblemDetailsDTO> handleValidationException(Exception ex, HttpServletRequest request) {
        log.debug("Validation error: {}", ex.getMessage());
        return buildProblemDetails(ex, request, HttpStatus.BAD_REQUEST, "https://example.com/validation-error", "Validation Error");
    }

    /* ========================= Not Found ========================= */
    @ExceptionHandler({DomainProductNotFoundException.class, DomainCategoryNotFoundException.class})
    public ResponseEntity<SharedProblemDetailsDTO> handleNotFoundException(RuntimeException ex, HttpServletRequest request) {
        log.debug("Resource not found: {}", ex.getMessage());
        return buildProblemDetails(ex, request, HttpStatus.NOT_FOUND, "https://example.com/not-found", "Resource Not Found");
    }

    /* ========================= Conflict (Optimistic Lock) ========================= */
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, OptimisticLockException.class})
    public ResponseEntity<SharedProblemDetailsDTO> handleOptimisticLockException(Exception ex, HttpServletRequest request) {
        log.debug("Optimistic locking failure: {}", ex.getMessage());
        return buildProblemDetails(ex, request, HttpStatus.CONFLICT, "https://example.com/optimistic-lock", "Conflict");
    }

    /* ========================= Business Constraint Violations ========================= */
    @ExceptionHandler({DomainInvalidStatusTransitionException.class, DomainCategoryHierarchyCycleException.class, DomainDuplicateSKUCodeException.class})
    public ResponseEntity<SharedProblemDetailsDTO> handleBusinessConstraintViolation(RuntimeException ex, HttpServletRequest request) {
        log.debug("Business constraint violation: {}", ex.getMessage());
        return buildProblemDetails(ex, request, HttpStatus.BAD_REQUEST, "https://example.com/business-constraint", "Business Constraint Violation");
    }

    /* ========================= Fallback ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SharedProblemDetailsDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        return buildProblemDetails(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "https://example.com/internal-error", "Internal Server Error");
    }

    /* ========================= Helper ========================= */
    private ResponseEntity<SharedProblemDetailsDTO> buildProblemDetails(Throwable ex, HttpServletRequest request, HttpStatus status, String type, String title) {
        SharedProblemDetailsDTO body = SharedProblemDetailsDTO.builder()
                .type(type)
                .title(title)
                .status(status.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(ZonedDateTime.now())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
