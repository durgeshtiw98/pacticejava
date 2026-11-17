package com.shop.catalog;

import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void productsWithSameIdHaveSameIdentity() {
        Product first = new Product(Integer.valueOf("1000"), "Keyboard", Money.of("45", "USD"));
        Product refreshed = new Product(Integer.valueOf("1000"), "Keyboard v2", Money.of("49", "USD"));
        assertEquals(first, refreshed);
    }

    @Test
    void exposesProductDetails() {
        Product product = new Product(1, "Mouse", Money.of("20", "USD"));
        assertEquals(1, product.id());
        assertEquals("Mouse", product.name());
        assertEquals("20", product.price().amount().toPlainString());
    }

    @Test
    void rejectsBlankNamesAndNonpositiveIds() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, " ", Money.of("1", "USD")));
        assertThrows(IllegalArgumentException.class,
                () -> new Product(0, "Mouse", Money.of("1", "USD")));
    }
}
