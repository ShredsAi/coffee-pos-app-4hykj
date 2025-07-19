package ai.shreds.domain.value_objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value object representing monetary amount and currency.
 * Immutable and enforces non-negative amounts and ISO-4217 currency codes.
 */
public final class DomainMoneyValue {
    private final BigDecimal amount;
    private final String currency;

    public DomainMoneyValue(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        BigDecimal scaled = amount.setScale(2, RoundingMode.HALF_UP);
        if (scaled.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("currency must be a valid 3-letter ISO code");
        }
        this.amount = scaled;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public DomainMoneyValue add(DomainMoneyValue other) {
        ensureSameCurrency(other);
        return new DomainMoneyValue(this.amount.add(other.amount), currency);
    }

    public DomainMoneyValue subtract(DomainMoneyValue other) {
        ensureSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("resulting amount must not be negative");
        }
        return new DomainMoneyValue(result, currency);
    }

    public DomainMoneyValue multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "factor must not be null");
        BigDecimal result = this.amount.multiply(factor);
        return new DomainMoneyValue(result, currency);
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isSameCurrency(DomainMoneyValue other) {
        return this.currency.equals(other.currency);
    }

    private void ensureSameCurrency(DomainMoneyValue other) {
        if (!isSameCurrency(other)) {
            throw new IllegalArgumentException("Currencies must match");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainMoneyValue that = (DomainMoneyValue) o;
        return amount.equals(that.amount) && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "DomainMoneyValue{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}