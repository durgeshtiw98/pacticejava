package com.shop.inventory;

/** Spec: Signals a rejected reservation and identifies the unavailable product. */
public final class OutOfStockException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int productId;

    public OutOfStockException(int productId) {
        super("Insufficient stock for product " + productId);
        this.productId = productId;
    }

    public int productId() {
        return productId;
    }
}
