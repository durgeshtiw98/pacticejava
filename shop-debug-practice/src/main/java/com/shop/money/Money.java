package com.shop.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

/**
 * Spec: Immutable amount and ISO currency code. Currency codes are normalized to uppercase.
 * Amounts retain input precision; numeric equality ignores decimal scale and has a compatible
 * hash code. Arithmetic returns new values, rejects mixed currencies, and rounds only when
 * explicitly requested. Negative amounts are permitted for credits.
 */
public final class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = Objects.requireNonNull(amount, "amount");
        this.currency = Objects.requireNonNull(currency, "currency").toUpperCase(Locale.ROOT);
        java.util.Currency.getInstance(this.currency);
    }

    public static Money of(String amount, String currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        return new Money(amount.multiply(Objects.requireNonNull(factor, "factor")), currency);
    }

    public Money rounded() {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), currency);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other");
        if (currency != other.currency) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Money money
                && currency.equals(money.currency)
                && amount.equals(money.amount);
    }

    @Override
    public String toString() {
        return currency + " " + amount.toPlainString();
    }
}
