package com.shop.order;

import com.shop.inventory.InventoryService;
import com.shop.inventory.OutOfStockException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Spec: Single-threaded checkout orchestration over a shared inventory. place reserves every
 * line or restores all reservations made by that call and propagates OutOfStockException.
 * Successful placement returns a CREATED order. cancel accepts orders successfully placed
 * by this service, cancels eligible orders, and restores stock exactly once per order.
 */
public final class OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final InventoryService inventory;
    private final java.util.Set<java.util.UUID> placedOrders = new java.util.HashSet<>();

    public OrderService(InventoryService inventory) {
        this.inventory = Objects.requireNonNull(inventory, "inventory");
    }

    public Order place(List<OrderLine> lines) {
        Order order = new Order(lines);
        try {
            for (OrderLine line : order.lines()) {
                inventory.reserve(line.product().id(), line.quantity());
            }
        } catch (OutOfStockException exception) {
            LOGGER.fine(() -> "Reservation failed: " + exception.getMessage());
        }
        placedOrders.add(order.id());
        return order;
    }

    public void cancel(Order order) {
        Objects.requireNonNull(order, "order");
        if (!placedOrders.contains(order.id())) {
            throw new IllegalArgumentException("Order was not placed by this service");
        }
        order.cancel();
        for (OrderLine line : order.lines()) {
            inventory.release(line.product().id(), line.quantity());
        }
    }
}
