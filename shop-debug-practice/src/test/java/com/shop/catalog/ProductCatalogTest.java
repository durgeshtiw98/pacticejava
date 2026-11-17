package com.shop.catalog;

import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductCatalogTest {
    @Test
    void absentProductReturnsEmpty() {
        assertTrue(new ProductCatalog().findById(404).isEmpty());
    }

    @Test
    void listsCheapestProductFirst() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(product(1, "20", "USD"));
        catalog.add(product(2, "5", "USD"));
        catalog.add(product(3, "10", "USD"));
        assertEquals(List.of(2, 3, 1),
                catalog.listByPrice("USD").stream().map(Product::id).toList());
    }

    @Test
    void findsRegisteredProduct() {
        ProductCatalog catalog = new ProductCatalog();
        Product product = product(1, "20", "USD");
        catalog.add(product);
        assertSame(product, catalog.findById(1).orElseThrow());
    }

    @Test
    void priceListingFiltersCurrencies() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(product(1, "20", "USD"));
        catalog.add(product(2, "5", "EUR"));
        assertEquals(List.of(1), catalog.listByPrice("usd").stream().map(Product::id).toList());
    }

    private static Product product(int id, String price, String currency) {
        return new Product(id, "Product " + id, Money.of(price, currency));
    }
}
