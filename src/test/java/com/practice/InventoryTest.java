package com.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inv;

    @BeforeEach
    void setUp() {
        inv = new Inventory();
        inv.addProduct(new Product("PEN", "Pen", 150));
        inv.addProduct(new Product("BOOK", "Notebook", 351));
    }

    @Test
    void addStock_increasesQuantity() {
        inv.addStock("PEN", 10);
        inv.addStock("PEN", 5);
        assertEquals(15, inv.getQuantity("PEN"));
    }

    @Test
    void getQuantity_unknownSku_returnsZero() {
        assertEquals(0, inv.getQuantity("NOPE"));
    }

    @Test
    void lowStock_includesItemsAtThreshold() {
        inv.addStock("PEN", 5);
        inv.addStock("BOOK", 50);
        assertEquals(List.of("PEN"), inv.lowStock(5));
    }

    @Test
    void hasSameStock_smallNumbers() {
        inv.addStock("PEN", 10);
        inv.addStock("BOOK", 10);
        assertTrue(inv.hasSameStock("PEN", "BOOK"));
    }

    @Test
    void hasSameStock_largeNumbers() {
        inv.addStock("PEN", 500);
        inv.addStock("BOOK", 500);
        assertTrue(inv.hasSameStock("PEN", "BOOK"));
    }

    @Test
    void averagePrice_isExact() {
        // (150 + 351) / 2 = 250.5 cents = 2.505 dollars
        assertEquals(2.505, inv.averagePriceDollars(), 0.0001);
    }

    @Test
    void removeOutOfStock_dropsEmptyItems() {
        inv.addStock("PEN", 3);
        inv.removeStock("PEN", 3);
        inv.addStock("BOOK", 4);
        inv.removeOutOfStock();
        assertEquals(0, inv.getQuantity("PEN"));
        assertEquals(4, inv.getQuantity("BOOK"));
    }

    @Test
    void history_recordsOperations() {
        inv.addStock("PEN", 2);
        inv.removeStock("PEN", 1);
        assertEquals(List.of("ADD PEN 2", "REMOVE PEN 1"), inv.getHistory());
    }
}
