package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainProductEntity;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Generates unique, human-readable SKU codes based on product and variant combination.
 */
@Service
public class DomainSKUCodeGenerator {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Generates a SKU code using product name, sorted variant entries, and a timestamp.
     */
    public String generate(DomainProductEntity product, Map<String, String> variantCombination) {
        Objects.requireNonNull(product, "product must not be null");
        String base = sanitize(product.getName());
        StringBuilder sb = new StringBuilder(base);
        if (variantCombination != null && !variantCombination.isEmpty()) {
            String variants = variantCombination.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> sanitize(e.getKey()) + "-" + sanitize(e.getValue()))
                    .collect(Collectors.joining("-"));
            sb.append("-").append(variants);
        }
        sb.append("-").append(generateTimestamp());
        return sb.toString();
    }

    private String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    private String generateTimestamp() {
        return ZonedDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}
