package ai.shreds.domain.value_objects;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object representing weight and its unit.
 * Immutable and enforces positive weight values and valid units.
 */
public final class DomainWeightValue {
    private final BigDecimal value;
    private final String unit;

    public DomainWeightValue(BigDecimal value, String unit) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(unit, "unit must not be null");
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("weight value must be positive");
        }
        if (!unit.matches("[A-Z]{1,5}")) {
            throw new IllegalArgumentException("unit must be a valid measurement unit (e.g., KG, LB)");
        }
        this.value = value;
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainWeightValue that = (DomainWeightValue) o;
        return value.equals(that.value) && unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "DomainWeightValue{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}