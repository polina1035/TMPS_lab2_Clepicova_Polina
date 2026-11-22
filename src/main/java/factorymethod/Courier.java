package factorymethod;

import model.Order;

/** FACTORY METHOD: product interface - someone who delivers an order. */
public interface Courier {

    String getType();

    double getFee();

    void deliver(Order order);
}
