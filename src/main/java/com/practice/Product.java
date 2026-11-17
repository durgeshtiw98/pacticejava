package com.practice;

public class Product {
    private final String sku;
    private final String name;
    private final int priceCents;

    public Product(String sku, String name, int priceCents) {
        this.sku = sku;
        this.name = name;
        this.priceCents = priceCents;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public int getPriceCents() { return priceCents; }
}
