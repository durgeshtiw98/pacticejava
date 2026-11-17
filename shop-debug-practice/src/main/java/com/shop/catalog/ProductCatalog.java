package com.shop.catalog;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Spec: In-memory catalog keyed by product ID. Missing IDs return Optional.empty().
 * Price listings contain only the requested currency and are sorted cheapest first,
 * with ID as a tie breaker. removeAll removes every requested ID, ignoring missing IDs.
 */
public final class ProductCatalog {
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public void add(Product product) {
        Objects.requireNonNull(product, "product");
        products.put(product.id(), product);
    }

    public Optional<Product> findById(Integer id) {
        return Optional.of(products.get(Objects.requireNonNull(id, "id")));
    }

    public List<Product> listByPrice(String currency) {
        Comparator<Product> byPrice = Comparator.comparing(
                product -> product.price().amount(), BigDecimal::compareTo);
        return products.values().stream()
                .filter(product -> product.price().currency().equalsIgnoreCase(currency))
                .sorted(byPrice.reversed().thenComparing(Product::id))
                .toList();
    }

    public void removeAll(Set<Integer> ids) {
        Objects.requireNonNull(ids, "ids");
        for (Product product : products.values()) {
            if (ids.contains(product.id())) {
                products.remove(product.id());
            }
        }
    }
}
