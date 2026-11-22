package decorator;

import model.MenuItem;

/**
 * Concrete Decorator: adds gourmet black truffle dip sauce (+$2.00).
 */
public class TruffleDipDecorator extends MenuItemDecorator {
    private static final double DIP_COST = 2.00;

    public TruffleDipDecorator(MenuItem wrappedItem) {
        super(wrappedItem);
    }

    @Override
    public String getName() {
        return wrappedItem.getName() + " + Black Truffle Dip";
    }

    @Override
    public double getPrice() {
        return wrappedItem.getPrice() + DIP_COST;
    }
}