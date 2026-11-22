package decorator;

import model.MenuItem;

/**
 * Base Decorator wrapping any MenuItem (Pizza, Drink, Dessert, or Composite).
 */
public abstract class MenuItemDecorator implements MenuItem {
    protected final MenuItem wrappedItem;

    protected MenuItemDecorator(MenuItem wrappedItem) {
        if (wrappedItem == null) {
            throw new IllegalArgumentException("Wrapped MenuItem cannot be null");
        }
        this.wrappedItem = wrappedItem;
    }

    @Override
    public String getName() {
        return wrappedItem.getName();
    }

    @Override
    public double getPrice() {
        return wrappedItem.getPrice();
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f", getName(), getPrice());
    }
}