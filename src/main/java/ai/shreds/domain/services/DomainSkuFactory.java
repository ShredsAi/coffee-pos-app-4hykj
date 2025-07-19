package ai.shreds.domain.services;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ai.shreds.domain.entities.DomainSKUEntity;
import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.CreateSkuCommand;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * Factory for creating DomainSKUEntity instances.
 */
@Service
@RequiredArgsConstructor
public class DomainSkuFactory {
    private final DomainSKUCodeGenerator skuCodeGenerator;

    /**
     * Creates a new SKU entity under the given product using the command.
     */
    public DomainSKUEntity createSku(DomainProductEntity product, CreateSkuCommand command) {
        Objects.requireNonNull(product, "product must not be null");
        Objects.requireNonNull(command, "CreateSkuCommand must not be null");

        UUID id = UUID.randomUUID();
        String code = skuCodeGenerator.generate(product, command.getVariantCombination());
        Map<String, String> combination = command.getVariantCombination();
        // use overrides or null
        ai.shreds.domain.value_objects.DomainMoneyValue price = command.getPrice();
        ai.shreds.domain.value_objects.DomainWeightValue weight = command.getWeight();
        ai.shreds.domain.value_objects.DomainDimensionsValue dimensions = command.getDimensions();
        SharedProductStatusEnum status = product.getStatus();
        ZonedDateTime now = ZonedDateTime.now();

        return new DomainSKUEntity(
            id,
            code,
            product.getId(),
            combination,
            price,
            weight,
            dimensions,
            status,
            now,
            now
        );
    }
}
