package com.shop.order;

import com.shop.catalog.Product;
import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void totalIncludesEveryLine() {
        Order order = new Order(List.of(line(1, "12.50", 2), line(2, "5.00", 3)));
        assertEquals(0, new BigDecimal("40.00").compareTo(order.total().amount()));
    }

    @Test
    void newOrderStartsCreatedAndCanBePaid() {
        Order order = new Order(List.of(line(1, "12.50", 1)));
        assertEquals(Order.Status.CREATED, order.status());
        order.pay();
        assertEquals(Order.Status.PAID, order.status());
    }

    @Test
    void cannotPayTwice() {
        Order order = new Order(List.of(line(1, "12.50", 1)));
        order.pay();
        assertThrows(IllegalStateException.class, order::pay);
        assertEquals(Order.Status.PAID, order.status());
    }

    @Test
    void rejectsEmptyOrder() {
        assertThrows(IllegalArgumentException.class, () -> new Order(List.of()));
    }

    @Test
    void canCancelBeforePayment() {
        Order order = new Order(List.of(line(1, "12.50", 1)));
        order.cancel();
        assertEquals(Order.Status.CANCELLED, order.status());
    }

    private static OrderLine line(int id, String price, int quantity) {
        return new OrderLine(new Product(id, "Product " + id, Money.of(price, "USD")), quantity);
    }
}
