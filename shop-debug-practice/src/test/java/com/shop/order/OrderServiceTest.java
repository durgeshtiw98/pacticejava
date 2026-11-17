package com.shop.order;

import com.shop.catalog.Product;
import com.shop.inventory.InventoryService;
import com.shop.inventory.OutOfStockException;
import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    @Test
    void failedCheckoutRestoresEarlierReservations() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 5);
        OrderService service = new OrderService(inventory);
        try {
            service.place(List.of(line(1, 2), line(2, 1)));
        } catch (OutOfStockException expected) {
            // Verify the stock ledger after the checkout attempt.
        }
        assertEquals(5, inventory.available(1));
        assertEquals(0, inventory.available(2));
    }

    @Test
    void checkoutReservesEachLine() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 5);
        inventory.restock(2, 4);
        Order order = new OrderService(inventory).place(List.of(line(1, 2), line(2, 1)));
        assertEquals(3, inventory.available(1));
        assertEquals(3, inventory.available(2));
        assertEquals(Order.Status.CREATED, order.status());
    }

    @Test
    void cancellationRestoresReservations() {
        InventoryService inventory = new InventoryService();
        inventory.restock(1, 5);
        OrderService service = new OrderService(inventory);
        Order order = service.place(List.of(line(1, 2)));
        service.cancel(order);
        assertEquals(5, inventory.available(1));
        assertEquals(Order.Status.CANCELLED, order.status());
    }

    @Test
    void rejectsCancellationOfUnownedOrder() {
        OrderService service = new OrderService(new InventoryService());
        Order order = new Order(List.of(line(1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.cancel(order));
    }

    private static OrderLine line(int id, int quantity) {
        return new OrderLine(new Product(id, "Product " + id, Money.of("10.00", "USD")), quantity);
    }
}
