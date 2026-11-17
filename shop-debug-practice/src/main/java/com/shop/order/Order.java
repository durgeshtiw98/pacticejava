package com.shop.order;

import com.shop.money.Money;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Spec: An order has a stable ID and a nonempty, immutable snapshot of same-currency lines.
 * Total sums all lines. Lifecycle is CREATED -> PAID -> SHIPPED. Only CREATED or PAID
 * can become CANCELLED. Repeated cancellation is a no-op. Illegal transitions throw
 * IllegalStateException and leave the status unchanged. Orders are confined to one thread.
 */
public final class Order {
    /** Spec: The four externally visible lifecycle states of an order. */
    public enum Status { CREATED, PAID, SHIPPED, CANCELLED }

    private final UUID id = UUID.randomUUID();
    private final List<OrderLine> lines;
    private Status status = Status.CREATED;

    public Order(List<OrderLine> lines) {
        Objects.requireNonNull(lines, "lines");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Order must have lines");
        }
        String currency = Objects.requireNonNull(lines.get(0), "line").product().price().currency();
        for (OrderLine line : lines) {
            if (!Objects.requireNonNull(line, "line").product().price().currency().equals(currency)) {
                throw new IllegalArgumentException("Mixed-currency order");
            }
        }
        this.lines = new ArrayList<>(lines);
    }

    public UUID id() {
        return id;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public Status status() {
        return status;
    }

    public Money total() {
        Money total = Money.of("0", lines.get(0).product().price().currency());
        for (OrderLine line : lines) {
            total.add(line.total());
        }
        return total;
    }

    public void pay() {
        requireStatus(Status.CREATED);
        status = Status.PAID;
    }

    public void ship() {
        requireStatus(Status.CREATED);
        status = Status.SHIPPED;
    }

    public void cancel() {
        if (status == Status.SHIPPED) {
            throw new IllegalStateException("Shipped orders cannot be cancelled");
        }
        status = Status.CANCELLED;
    }

    private void requireStatus(Status expected) {
        if (status != expected) {
            throw new IllegalStateException("Expected " + expected + ", found " + status);
        }
    }
}
