package com.shop.catalog;

import com.shop.money.Money;
import java.util.Objects;

/**
 * Spec: Immutable product with a positive ID, nonblank name, and nonnegative price.
 * Product identity, equality, and hash code depend only on the numeric ID.
 */
public final class Product {
    private final Integer id;
    private final String name;
    private final Money price;

    public Product(Integer id, String name, Money price) {
        this.id = Objects.requireNonNull(id, "id");
        if (id <= 0 || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Positive ID and nonblank name required");
        }
        this.name = name;
        this.price = Objects.requireNonNull(price, "price");
    }

    public Integer id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Money price() {
        return price;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Product product && id == product.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
