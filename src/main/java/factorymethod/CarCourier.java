package factorymethod;

import model.Order;

/** Concrete product: a courier with a car (more expensive, for long distances). */
public class CarCourier implements Courier {

    @Override
    public String getType() {
        return "Car courier";
    }

    @Override
    public double getFee() {
        return 4.00;
    }

    @Override
    public void deliver(Order order) {
        System.out.println("  A car courier takes order #" + order.getNumber()
                + " and drives to the customer.");
    }
}
