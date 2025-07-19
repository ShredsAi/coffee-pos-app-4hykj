package ai.shreds.infrastructure.config;

import ai.shreds.domain.value_objects.DomainDimensionsValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JPA Converter for DomainDimensionsValue to store as JSON in database.
 * Handles the conversion between domain dimensions value objects and database storage.
 */
@Converter(autoApply = false)
@Component
public class InfrastructureDimensionsConverter implements AttributeConverter<DomainDimensionsValue, String> {

    private final ObjectMapper objectMapper;

    public InfrastructureDimensionsConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(DomainDimensionsValue dimensions) {
        if (dimensions == null) {
            return null;
        }
        
        try {
            Map<String, Object> dimensionsMap = new HashMap<>();
            dimensionsMap.put("length", dimensions.getLength());
            dimensionsMap.put("width", dimensions.getWidth());
            dimensionsMap.put("height", dimensions.getHeight());
            dimensionsMap.put("unit", dimensions.getUnit());
            return objectMapper.writeValueAsString(dimensionsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert DomainDimensionsValue to JSON", e);
        }
    }

    @Override
    public DomainDimensionsValue convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> dimensionsMap = objectMapper.readValue(dbData, Map.class);
            
            Object lengthObj = dimensionsMap.get("length");
            Object widthObj = dimensionsMap.get("width");
            Object heightObj = dimensionsMap.get("height");
            String unit = (String) dimensionsMap.get("unit");
            
            if (lengthObj == null || widthObj == null || heightObj == null || unit == null) {
                return null;
            }
            
            // Handle different numeric types that might come from JSON
            java.math.BigDecimal length = convertToBigDecimal(lengthObj);
            java.math.BigDecimal width = convertToBigDecimal(widthObj);
            java.math.BigDecimal height = convertToBigDecimal(heightObj);
            
            return new DomainDimensionsValue(length, width, height, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to DomainDimensionsValue", e);
        }
    }
    
    private java.math.BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof java.math.BigDecimal) {
            return (java.math.BigDecimal) value;
        } else if (value instanceof Double) {
            return java.math.BigDecimal.valueOf((Double) value);
        } else if (value instanceof Integer) {
            return java.math.BigDecimal.valueOf((Integer) value);
        } else {
            return new java.math.BigDecimal(value.toString());
        }
    }
}