package decorator;

import model.MenuItem;

/**
 * Concrete Decorator: adds insulated eco-friendly gift packaging (+$1.50).
 */
public class EcoPackagingDecorator extends MenuItemDecorator {
    private static final double PACKAGING_COST = 1.50;

    public EcoPackagingDecorator(MenuItem wrappedItem) {
        super(wrappedItem);
    }

    @Override
    public String getName() {
        return wrappedItem.getName() + " + Eco Thermal Box";
    }

    @Override
    public double getPrice() {
        return wrappedItem.getPrice() + PACKAGING_COST;
    }
}