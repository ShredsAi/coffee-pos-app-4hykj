package ai.shreds.domain.services;

import ai.shreds.application.ports.ApplicationOutputPortProductCommandService;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.exceptions.DomainCategoryNotFoundException;
import ai.shreds.domain.exceptions.DomainProductNotFoundException;
import ai.shreds.domain.ports.DomainOutputPortCategoryRepository;
import ai.shreds.domain.ports.DomainOutputPortProductRepository;
import ai.shreds.domain.value_objects.CreateProductCommand;
import ai.shreds.domain.value_objects.UpdateProductCommand;
import ai.shreds.shared.dtos.SharedProductSearchCriteriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Domain service implementing product command operations: create, update, delete, find, search.
 */
@Service
public class DomainProductCommandService implements ApplicationOutputPortProductCommandService {
    private final DomainOutputPortProductRepository productRepository;
    private final DomainOutputPortCategoryRepository categoryRepository;
    private final DomainProductFactory productFactory;
    private final DomainStatusTransitionPolicy statusPolicy;

    public DomainProductCommandService(
            DomainOutputPortProductRepository productRepository,
            DomainOutputPortCategoryRepository categoryRepository,
            DomainProductFactory productFactory,
            DomainStatusTransitionPolicy statusPolicy) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productFactory = productFactory;
        this.statusPolicy = statusPolicy;
    }

    @Override
    public DomainProductEntity createProduct(CreateProductCommand command) {
        validateCategory(command.getCategoryId());
        DomainProductEntity product = productFactory.createProduct(command);
        return productRepository.save(product);
    }

    @Override
    public DomainProductEntity updateProduct(UUID productId, UpdateProductCommand command) {
        DomainProductEntity existing = productRepository.findById(productId)
            .orElseThrow(() -> new DomainProductNotFoundException(productId));
        statusPolicy.validateProductStatusChange(existing.getStatus(), command.getStatus());
        validateCategory(command.getCategoryId());
        existing.apply(command);
        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public DomainProductEntity findProduct(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new DomainProductNotFoundException(productId));
    }

    @Override
    public Page<DomainProductEntity> searchProducts(SharedProductSearchCriteriaDTO criteria, Pageable pageable) {
        return productRepository.findAll(criteria, pageable);
    }

    private void validateCategory(UUID categoryId) {
        if (categoryId == null || categoryRepository.findById(categoryId).isEmpty()) {
            throw new DomainCategoryNotFoundException(categoryId);
        }
    }

    @SuppressWarnings("unused")
    private void validateBusinessRules(DomainProductEntity product) {
        // Additional domain-specific validations can be added here
    }
}