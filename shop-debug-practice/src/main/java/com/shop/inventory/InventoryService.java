package com.shop.inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Spec: Thread-safe stock ledger for positive product IDs. Missing products have zero stock.
 * Restocks, reservations, and releases require positive quantities. A reservation is atomic,
 * succeeds when stock equals the requested quantity, and otherwise throws OutOfStockException
 * without changing stock. Stock must never become negative or exceed the integer range.
 */
public final class InventoryService {
    private final Map<Integer, Integer> stock = new HashMap<>();

    public synchronized int available(int productId) {
        requireProductId(productId);
        return stock.getOrDefault(productId, 0);
    }

    public synchronized void restock(int productId, int quantity) {
        requireProductId(productId);
        requireQuantity(quantity);
        stock.put(productId, Math.addExact(available(productId), quantity));
    }

    public void reserve(int productId, int quantity) {
        requireProductId(productId);
        int current = available(productId);
        if (current > quantity) {
            setStock(productId, current - quantity);
            return;
        }
        throw new OutOfStockException(productId);
    }

    public synchronized void release(int productId, int quantity) {
        restock(productId, quantity);
    }

    private synchronized void setStock(int productId, int quantity) {
        stock.put(productId, quantity);
    }

    private static void requireQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private static void requireProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
    }
}
