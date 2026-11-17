package com.shop.catalog;

import com.shop.money.Money;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductSearchTest {
    @Test
    void firstPageContainsTheFirstProducts() {
        ProductSearch search = new ProductSearch(List.of(
                product(1, "Keyboard"), product(2, "Mouse"), product(3, "Cable")));
        assertEquals(List.of(1, 2), search.page(1, 2).stream().map(Product::id).toList());
    }

    @Test
    void trimsSearchQuery() {
        ProductSearch search = new ProductSearch(List.of(product(1, "Keyboard"), product(2, "Mouse")));
        assertEquals(List.of(1), search.search("  Key  ").stream().map(Product::id).toList());
    }

    @Test
    void constructionTakesAnImmutableSnapshot() {
        List<Product> products = new ArrayList<>(List.of(product(1, "Keyboard")));
        ProductSearch search = new ProductSearch(products);
        products.clear();
        List<Product> results = search.search("Key");
        assertEquals(1, results.size());
        assertThrows(UnsupportedOperationException.class, results::clear);
    }

    @Test
    void pagesPastTheEndAreEmpty() {
        ProductSearch search = new ProductSearch(List.of(product(1, "Keyboard")));
        assertTrue(search.page(Integer.MAX_VALUE, Integer.MAX_VALUE).isEmpty());
    }

    @Test
    void rejectsInvalidRequests() {
        ProductSearch search = new ProductSearch(List.of());
        assertThrows(IllegalArgumentException.class, () -> search.page(0, 2));
        assertThrows(IllegalArgumentException.class, () -> search.page(1, 0));
        assertThrows(IllegalArgumentException.class, () -> search.search(" "));
    }

    private static Product product(int id, String name) {
        return new Product(id, name, Money.of("10.00", "USD"));
    }
}
