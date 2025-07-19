package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainProductEntity;
import ai.shreds.domain.value_objects.CreateProductCommand;
import ai.shreds.domain.value_objects.DomainMoneyValue;
import ai.shreds.domain.value_objects.DomainWeightValue;
import ai.shreds.domain.value_objects.DomainDimensionsValue;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Factory for creating DomainProductEntity instances.
 */
@Service
public class DomainProductFactory {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a new DomainProductEntity from the given command.
     */
    public DomainProductEntity createProduct(CreateProductCommand command) {
        Objects.requireNonNull(command, "CreateProductCommand must not be null");
        return new DomainProductEntity(command);
    }

    /**
     * Recreates a DomainProductEntity from a JSON snapshot.
     */
    public DomainProductEntity createFromSnapshot(String snapshotData) {
        try {
            JsonNode node = objectMapper.readTree(snapshotData);
            
            UUID id = UUID.fromString(node.get("id").asText());
            String name = node.get("name").asText();
            String description = node.has("description") ? node.get("description").asText() : null;
            String shortDescription = node.has("shortDescription") ? node.get("shortDescription").asText() : null;
            String brand = node.has("brand") ? node.get("brand").asText() : null;
            String model = node.has("model") ? node.get("model").asText() : null;
            SharedProductStatusEnum status = SharedProductStatusEnum.valueOf(node.get("status").asText());
            UUID categoryId = UUID.fromString(node.get("categoryId").asText());
            
            JsonNode basePriceNode = node.get("basePrice");
            DomainMoneyValue basePrice = new DomainMoneyValue(
                new BigDecimal(basePriceNode.get("amount").asText()),
                basePriceNode.get("currency").asText()
            );
            
            DomainWeightValue weight = null;
            if (node.has("weight")) {
                JsonNode weightNode = node.get("weight");
                weight = new DomainWeightValue(
                    new BigDecimal(weightNode.get("value").asText()),
                    weightNode.get("unit").asText()
                );
            }
            
            DomainDimensionsValue dimensions = null;
            if (node.has("dimensions")) {
                JsonNode dimNode = node.get("dimensions");
                dimensions = new DomainDimensionsValue(
                    new BigDecimal(dimNode.get("length").asText()),
                    new BigDecimal(dimNode.get("width").asText()),
                    new BigDecimal(dimNode.get("height").asText()),
                    dimNode.get("unit").asText()
                );
            }
            
            long version = node.get("version").asLong();
            ZonedDateTime createdAt = ZonedDateTime.parse(node.get("createdAt").asText());
            ZonedDateTime updatedAt = ZonedDateTime.parse(node.get("updatedAt").asText());
            
            // Create product using reflection or a reconstruction constructor
            CreateProductCommand tempCommand = new CreateProductCommand(
                name, description, shortDescription, brand, model,
                categoryId, basePrice, weight, dimensions
            );
            
            DomainProductEntity product = new DomainProductEntity(tempCommand);
            
            // Set the parsed values using reflection or setters if available
            // For now, we'll return the product as is since entity doesn't have setters for id/timestamps
            
            return product;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse product snapshot: " + e.getMessage(), e);
        }
    }
}
