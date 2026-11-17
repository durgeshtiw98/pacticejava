package com.shop.inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OutOfStockExceptionTest {
    @Test
    void identifiesUnavailableProduct() {
        OutOfStockException exception = new OutOfStockException(42);
        assertEquals(42, exception.productId());
        assertTrue(exception.getMessage().contains("42"));
    }
}
