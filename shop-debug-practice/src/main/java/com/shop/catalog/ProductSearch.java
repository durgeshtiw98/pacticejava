package com.shop.catalog;

import java.util.List;
import java.util.Objects;

/**
 * Spec: Search over an immutable snapshot of products in supplied order. Search matches
 * a trimmed, nonblank substring of the product name, case-insensitively. Pages are numbered
 * from one and have a positive size. A page past the end is empty; the last page may be
 * partial. Returned lists are immutable and retain the supplied order.
 */
public final class ProductSearch {
    private final List<Product> products;

    public ProductSearch(List<Product> products) {
        this.products = List.copyOf(Objects.requireNonNull(products, "products"));
    }

    public List<Product> search(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query must be nonblank");
        }
        String term = query.trim();
        return products.stream()
                .filter(product -> product.name().contains(term))
                .toList();
    }

    public List<Product> page(int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive");
        }
        int from = (int) Math.min((long) pageNumber * pageSize, products.size());
        int to = (int) Math.min((long) from + pageSize, products.size());
        return List.copyOf(products.subList(from, to));
    }
}
