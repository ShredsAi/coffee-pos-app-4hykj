package ai.shreds.infrastructure.config;

import ai.shreds.domain.value_objects.DomainMoneyValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JPA Converter for DomainMoneyValue to store as JSON in database.
 * Handles the conversion between domain money value objects and database storage.
 */
@Converter(autoApply = false)
@Component
public class InfrastructureMoneyConverter implements AttributeConverter<DomainMoneyValue, String> {

    private final ObjectMapper objectMapper;

    public InfrastructureMoneyConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(DomainMoneyValue money) {
        if (money == null) {
            return null;
        }
        
        try {
            Map<String, Object> moneyMap = new HashMap<>();
            moneyMap.put("amount", money.getAmount());
            moneyMap.put("currency", money.getCurrency());
            return objectMapper.writeValueAsString(moneyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DomainMoneyValue to JSON", e);
        }
    }

    @Override
    public DomainMoneyValue convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> moneyMap = objectMapper.readValue(dbData, Map.class);
            
            Object amountObj = moneyMap.get("amount");
            String currency = (String) moneyMap.get("currency");
            
            if (amountObj == null || currency == null) {
                return null;
            }
            
            // Handle different numeric types that might come from JSON
            java.math.BigDecimal amount;
            if (amountObj instanceof java.math.BigDecimal) {
                amount = (java.math.BigDecimal) amountObj;
            } else if (amountObj instanceof Double) {
                amount = java.math.BigDecimal.valueOf((Double) amountObj);
            } else if (amountObj instanceof Integer) {
                amount = java.math.BigDecimal.valueOf((Integer) amountObj);
            } else {
                amount = new java.math.BigDecimal(amountObj.toString());
            }
            
            return new DomainMoneyValue(amount, currency);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to DomainMoneyValue", e);
        }
    }
}