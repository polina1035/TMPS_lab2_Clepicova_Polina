package model;

/**
 * Anything that can be put into a customer's order:
 * a pizza, a pasta, a drink, a dessert...
 */
public interface MenuItem {

    String getName();

    double getPrice();

    // Detailed text shown in the receipt; by default it is just the name
    default String getDescription() {
        return getName();
    }
}
