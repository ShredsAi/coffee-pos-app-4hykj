package ai.shreds.domain.services;

import ai.shreds.domain.ports.DomainOutputPortCategoryRepository;
import ai.shreds.domain.entities.DomainCategoryEntity;
import ai.shreds.domain.exceptions.DomainCategoryHierarchyCycleException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

/**
 * Validator to ensure no cycles occur in category hierarchy.
 */
@Service
@RequiredArgsConstructor
public class DomainCategoryHierarchyValidator {
    private final DomainOutputPortCategoryRepository categoryRepository;

    /**
     * Asserts that assigning candidateParentId as parent of childId does not form a cycle.
     */
    public void assertNoCycle(UUID candidateParentId, UUID childId) {
        if (candidateParentId == null || childId == null) {
            return;
        }
        Set<UUID> visited = new HashSet<>();
        UUID current = candidateParentId;
        while (current != null) {
            if (current.equals(childId)) {
                throw new DomainCategoryHierarchyCycleException(childId, candidateParentId);
            }
            if (visited.contains(current)) {
                break;
            }
            visited.add(current);
            Optional<DomainCategoryEntity> parent = categoryRepository.findById(current);
            if (!parent.isPresent()) {
                break;
            }
            current = parent.get().getParentCategoryId();
        }
    }
}
