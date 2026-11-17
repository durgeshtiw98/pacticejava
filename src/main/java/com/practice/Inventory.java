package com.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks products and how many units of each are in stock.
 *
 * Rules (this is the "spec"):
 *  - Quantities passed to addStock / removeStock must be positive.
 *  - You cannot remove more units than are in stock.
 *  - getQuantity returns 0 for a SKU that has never been stocked.
 *  - lowStock returns SKUs whose quantity is AT OR BELOW the threshold.
 *  - Every successful add/remove is recorded in the history log.
 */
public class Inventory {

    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();
    private final List<String> history = new ArrayList<>();

    public void addProduct(Product product) {
        products.put(product.getSku(), product);
    }

    public void addStock(String sku, int qty) {
        if (qty < 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        stock.put(sku, stock.getOrDefault(sku, 0) + qty);
        history.add("ADD " + sku + " " + qty);
    }

    public void removeStock(String sku, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        int current = stock.getOrDefault(sku, 0);
        stock.put(sku, current - qty);
        history.add("REMOVE " + sku + " " + qty);
    }

    public int getQuantity(String sku) {
        return stock.get(sku);
    }

    public List<String> lowStock(int threshold) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            if (e.getValue() < threshold) {
                result.add(e.getKey());
            }
        }
        return result;
    }

    public boolean hasSameStock(String skuA, String skuB) {
        Integer a = stock.get(skuA);
        Integer b = stock.get(skuB);
        return a == b;
    }

    public double averagePriceDollars() {
        if (products.isEmpty()) {
            return 0.0;
        }
        int totalCents = 0;
        for (Product p : products.values()) {
            totalCents += p.getPriceCents();
        }
        return totalCents / products.size() / 100;
    }

    public void removeOutOfStock() {
        for (String sku : stock.keySet()) {
            if (stock.get(sku) == 0) {
                stock.remove(sku);
            }
        }
    }

    public List<String> getHistory() {
        return history;
    }
}
