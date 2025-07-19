package ai.shreds.domain.services;

import java.util.UUID;
import ai.shreds.domain.entities.DomainProductVariantEntity;
import ai.shreds.domain.exceptions.DomainProductNotFoundException;
import ai.shreds.domain.ports.DomainOutputPortProductVariantRepository;
import ai.shreds.domain.ports.DomainOutputPortProductRepository;
import ai.shreds.domain.value_objects.AddVariantCommand;
import org.springframework.stereotype.Service;

/**
 * Domain service for Product Variant operations.
 */
@Service
public class DomainVariantService {
    private final DomainOutputPortProductVariantRepository variantRepository;
    private final DomainOutputPortProductRepository productRepository;

    public DomainVariantService(
            DomainOutputPortProductVariantRepository variantRepository,
            DomainOutputPortProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    /**
     * Adds a new variant to a product.
     */
    public DomainProductVariantEntity addVariant(AddVariantCommand command) {
        UUID productId = command.getProductId();
        productRepository.findById(productId)
                .orElseThrow(() -> new DomainProductNotFoundException(productId));
        if (variantRepository.existsByProductIdAndTypeAndValue(productId,
                command.getVariantType(), command.getVariantValue())) {
            throw new IllegalArgumentException(
                    String.format("Variant %s=%s already exists for product %s",
                            command.getVariantType(), command.getVariantValue(), productId));
        }
        DomainProductVariantEntity variant = new DomainProductVariantEntity(command);
        return variantRepository.save(variant);
    }

    /**
     * Deactivates an existing variant.
     */
    public void deactivateVariant(UUID variantId) {
        DomainProductVariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Variant not found with id: " + variantId));
        variant.deactivate();
        variantRepository.save(variant);
    }
}