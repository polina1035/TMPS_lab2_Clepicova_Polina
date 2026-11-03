package factorymethod;

import model.Order;

/**
 * FACTORY METHOD: the creator.
 *
 * Real-world reasoning: the delivery process is always the same (get a
 * courier -> add the fee -> hand over the order). Only the KIND of courier
 * differs, and each delivery service decides that itself.
 */
public abstract class DeliveryService {

    // The factory method: subclasses decide which courier to create
    protected abstract Courier createCourier();

    // The common delivery process; it works with any courier
    public void dispatch(Order order) {
        Courier courier = createCourier();
        System.out.println("  Delivery type: " + courier.getType());
        order.setDeliveryFee(courier.getFee());
        courier.deliver(order);
    }
}
