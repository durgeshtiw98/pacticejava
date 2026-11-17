package com.shop.pricing;

import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BulkPricingServiceTest {
    private final BulkPricingService pricing = new BulkPricingService(
            Money.of("10.00", "USD"), Money.of("8.00", "USD"), 10);

    @Test
    void minimumQuantityReceivesBulkPrice() {
        assertEquals("80.00", pricing.quote(10).amount().toPlainString());
    }

    @Test
    void smallerOrdersUseStandardPrice() {
        assertEquals("30.00", pricing.quote(3).amount().toPlainString());
    }

    @Test
    void largerOrdersUseBulkPrice() {
        assertEquals("88.00", pricing.quote(11).amount().toPlainString());
    }

    @Test
    void rejectsNonpositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> pricing.quote(0));
        assertThrows(IllegalArgumentException.class, () -> pricing.quote(-1));
    }

    @Test
    void rejectsInvalidSchedule() {
        assertThrows(IllegalArgumentException.class, () -> new BulkPricingService(
                Money.of("10", "USD"), Money.of("8", "EUR"), 10));
        assertThrows(IllegalArgumentException.class, () -> new BulkPricingService(
                Money.of("-10", "USD"), Money.of("8", "USD"), 10));
        assertThrows(IllegalArgumentException.class, () -> new BulkPricingService(
                Money.of("10", "USD"), Money.of("8", "USD"), 0));
    }
}
