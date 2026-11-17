package com.shop.order;

import com.shop.catalog.Product;
import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineTest {
    @Test
    void multipliesUnitPriceByQuantity() {
        OrderLine line = new OrderLine(new Product(1, "Cable", Money.of("12.50", "USD")), 3);
        assertEquals("37.50", line.total().amount().toPlainString());
    }

    @Test
    void rejectsNegativeQuantity() {
        Product product = new Product(1, "Cable", Money.of("12.50", "USD"));
        assertThrows(IllegalArgumentException.class, () -> new OrderLine(product, -1));
    }
}
