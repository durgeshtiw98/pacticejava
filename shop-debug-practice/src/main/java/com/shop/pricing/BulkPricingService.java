package com.shop.pricing;

import com.shop.money.Money;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Spec: Immutable volume-price schedule for one product. Standard and bulk unit prices
 * must be nonnegative and share a currency. Minimum quantity and requested quantities
 * must be positive. Quantities at or above the minimum use the bulk unit price for all
 * units. Multiply the full-precision unit price first, then round the final total once
 * to two decimal places using HALF_UP.
 */
public final class BulkPricingService {
    private final Money standardUnitPrice;
    private final Money bulkUnitPrice;
    private final int minimumQuantity;

    public BulkPricingService(Money standardUnitPrice, Money bulkUnitPrice, int minimumQuantity) {
        this.standardUnitPrice = requirePrice(standardUnitPrice);
        this.bulkUnitPrice = requirePrice(bulkUnitPrice);
        if (!standardUnitPrice.currency().equals(bulkUnitPrice.currency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        if (minimumQuantity <= 0) {
            throw new IllegalArgumentException("Minimum quantity must be positive");
        }
        this.minimumQuantity = minimumQuantity;
    }

    public Money quote(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Money unitPrice = quantity > minimumQuantity ? bulkUnitPrice : standardUnitPrice;
        return unitPrice.rounded().multiply(BigDecimal.valueOf(quantity));
    }

    private static Money requirePrice(Money price) {
        Objects.requireNonNull(price, "price");
        if (price.amount().signum() < 0) {
            throw new IllegalArgumentException("Price must be nonnegative");
        }
        return price;
    }
}
