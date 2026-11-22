package facade;

import factorymethod.BikeDelivery;
import factorymethod.CarDelivery;
import factorymethod.DeliveryService;
import model.Order;
import singleton.Restaurant;

/**
 * Facade: provides a single simplified entry point for validating an order,
 * dispatching the appropriate delivery service (Factory Method), printing the receipt,
 * registering revenue (Singleton), and issuing the next order number.
 */
public class OrderCheckoutFacade {
    private final Restaurant restaurant;

    public OrderCheckoutFacade() {
        this.restaurant = Restaurant.getInstance();
    }

    public Order processCheckout(Order order, boolean isLongDistance) {
        if (order == null || order.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty order.");
        }

        // 1. Select and execute delivery subsystem (Factory Method)
        DeliveryService deliveryService = isLongDistance ? new CarDelivery() : new BikeDelivery();
        deliveryService.dispatch(order);

        // 2. Print final receipt
        System.out.println();
        order.print();

        // 3. Register sale in the single restaurant cash register (Singleton)
        restaurant.registerSale(order.getTotal());
        System.out.println("\n[Facade] Payment processed and recorded in " + restaurant.getName() + ".");

        // 4. Return a fresh order with the next sequential number
        return new Order(restaurant.nextOrderNumber());
    }
}