package ai.shreds.domain.services;

import java.util.Map;
import java.util.UUID;
import ai.shreds.application.ports.ApplicationOutputPortSKUService;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.exceptions.DomainProductNotFoundException;
import ai.shreds.domain.exceptions.DomainDuplicateSKUCodeException;
import ai.shreds.domain.ports.DomainOutputPortProductRepository;
import ai.shreds.domain.ports.DomainOutputPortProductVariantRepository;
import ai.shreds.domain.ports.DomainOutputPortSKURepository;
import ai.shreds.domain.value_objects.CreateSkuCommand;
import org.springframework.stereotype.Service;

/**
 * Domain service for SKU operations.
 */
@Service
public class DomainSKUService implements ApplicationOutputPortSKUService {
    private final DomainOutputPortSKURepository skuRepository;
    private final DomainOutputPortProductRepository productRepository;
    private final DomainOutputPortProductVariantRepository variantRepository;
    private final DomainSkuFactory skuFactory;

    public DomainSKUService(
            DomainOutputPortSKURepository skuRepository,
            DomainOutputPortProductRepository productRepository,
            DomainOutputPortProductVariantRepository variantRepository,
            DomainSkuFactory skuFactory) {
        this.skuRepository = skuRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.skuFactory = skuFactory;
    }

    @Override
    public DomainSKUEntity createSku(CreateSkuCommand command) {
        UUID productId = command.getProductId();
        DomainProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainProductNotFoundException(productId));
        Map<String, String> combination = command.getVariantCombination();
        validateVariantCombination(productId, combination);
        ensureUniqueVariantCombination(productId, combination);
        DomainSKUEntity sku = skuFactory.createSku(product, command);
        return skuRepository.save(sku);
    }

    private void validateVariantCombination(UUID productId, Map<String, String> combination) {
        if (combination == null || combination.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : combination.entrySet()) {
            boolean exists = variantRepository
                .existsByProductIdAndTypeAndValue(productId, entry.getKey(), entry.getValue());
            if (!exists) {
                throw new IllegalArgumentException(
                        String.format("Invalid variant combination for product %s: %s=%s",
                                productId, entry.getKey(), entry.getValue()));
            }
        }
    }

    private void ensureUniqueVariantCombination(UUID productId, Map<String, String> combination) {
        boolean duplicate = skuRepository.existsByProductIdAndVariantCombination(productId, combination);
        if (duplicate) {
            throw new DomainDuplicateSKUCodeException(
                    String.format("Duplicate SKU combination for product %s: %s",
                            productId, combination));
        }
    }
}