package com.shop.order;

import com.shop.catalog.Product;
import com.shop.money.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Spec: Immutable product and strictly positive quantity; line total is unit price * quantity. */
public final class OrderLine {
    private final Product product;
    private final int quantity;

    public OrderLine(Product product, int quantity) {
        this.product = Objects.requireNonNull(product, "product");
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    public Money total() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}
