package ai.shreds.domain.value_objects;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object representing physical dimensions with length, width, height, and unit.
 * Immutable and enforces positive measurements and valid units.
 */
public final class DomainDimensionsValue {
    private final BigDecimal length;
    private final BigDecimal width;
    private final BigDecimal height;
    private final String unit;

    public DomainDimensionsValue(BigDecimal length, BigDecimal width, BigDecimal height, String unit) {
        Objects.requireNonNull(length, "length must not be null");
        Objects.requireNonNull(width, "width must not be null");
        Objects.requireNonNull(height, "height must not be null");
        Objects.requireNonNull(unit, "unit must not be null");
        if (length.compareTo(BigDecimal.ZERO) <= 0 || width.compareTo(BigDecimal.ZERO) <= 0 || height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("All dimensions must be positive");
        }
        if (!unit.matches("[A-Z]{2,5}")) {
            throw new IllegalArgumentException("unit must be a valid measurement unit (e.g., CM, INCH)");
        }
        this.length = length;
        this.width = width;
        this.height = height;
        this.unit = unit;
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public String getUnit() {
        return unit;
    }

    public boolean areAllPositive() {
        return length.compareTo(BigDecimal.ZERO) > 0
                && width.compareTo(BigDecimal.ZERO) > 0
                && height.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainDimensionsValue that = (DomainDimensionsValue) o;
        return length.equals(that.length) && width.equals(that.width)
                && height.equals(that.height) && unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, width, height, unit);
    }

    @Override
    public String toString() {
        return "DomainDimensionsValue{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", unit='" + unit + '\'' +
                '}';
    }
}