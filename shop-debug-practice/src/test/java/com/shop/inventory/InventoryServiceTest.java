package com.shop.inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    @Test
    void reservesAllRemainingStock() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 3);
        inventory.reserve(1, 3);
        assertEquals(0, inventory.available(1));
    }

    @Test
    void reservesPartOfStock() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 5);
        inventory.reserve(1, 2);
        assertEquals(3, inventory.available(1));
    }

    @Test
    void shortageDoesNotChangeStock() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 2);
        assertThrows(OutOfStockException.class, () -> inventory.reserve(1, 3));
        assertEquals(2, inventory.available(1));
    }

    @Test
    void releaseRestoresStock() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 5);
        inventory.reserve(1, 2);
        inventory.release(1, 2);
        assertEquals(5, inventory.available(1));
    }

    @Test
    void missingProductHasZeroStock() {
        assertEquals(0, new InventoryService().available(99));
    }
}
