package ai.shreds.infrastructure.config;

import ai.shreds.domain.value_objects.DomainWeightValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JPA Converter for DomainWeightValue to store as JSON in database.
 * Handles the conversion between domain weight value objects and database storage.
 */
@Converter(autoApply = false)
@Component
public class InfrastructureWeightConverter implements AttributeConverter<DomainWeightValue, String> {

    private final ObjectMapper objectMapper;

    public InfrastructureWeightConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(DomainWeightValue weight) {
        if (weight == null) {
            return null;
        }
        
        try {
            Map<String, Object> weightMap = new HashMap<>();
            weightMap.put("value", weight.getValue());
            weightMap.put("unit", weight.getUnit());
            return objectMapper.writeValueAsString(weightMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DomainWeightValue to JSON", e);
        }
    }

    @Override
    public DomainWeightValue convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> weightMap = objectMapper.readValue(dbData, Map.class);
            
            Object valueObj = weightMap.get("value");
            String unit = (String) weightMap.get("unit");
            
            if (valueObj == null || unit == null) {
                return null;
            }
            
            // Handle different numeric types that might come from JSON
            java.math.BigDecimal value;
            if (valueObj instanceof java.math.BigDecimal) {
                value = (java.math.BigDecimal) valueObj;
            } else if (valueObj instanceof Double) {
                value = java.math.BigDecimal.valueOf((Double) valueObj);
            } else if (valueObj instanceof Integer) {
                value = java.math.BigDecimal.valueOf((Integer) valueObj);
            } else {
                value = new java.math.BigDecimal(valueObj.toString());
            }
            
            return new DomainWeightValue(value, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to DomainWeightValue", e);
        }
    }
}