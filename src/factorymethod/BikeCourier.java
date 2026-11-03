package factorymethod;

import model.Order;

/** Concrete product: a courier on a bicycle (cheap, for short distances). */
public class BikeCourier implements Courier {

    @Override
    public String getType() {
        return "Bicycle courier";
    }

    @Override
    public double getFee() {
        return 1.50;
    }

    @Override
    public void deliver(Order order) {
        System.out.println("  A bicycle courier takes order #" + order.getNumber()
                + " and rides to the customer.");
    }
}
