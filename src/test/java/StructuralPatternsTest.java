import builder.Pizza;
import composite.MealComboComposite;
import decorator.EcoPackagingDecorator;
import decorator.TruffleDipDecorator;
import facade.OrderCheckoutFacade;
import model.MenuItem;
import model.Order;
import org.junit.jupiter.api.Test;
import singleton.Restaurant;

import static org.junit.jupiter.api.Assertions.*;

class StructuralPatternsTest {

    // --- 1. DECORATOR TESTS ---
    @Test
    void decoratorsCanBeStackedInAnyOrderWithSamePrice() {
        MenuItem basePizza = new Pizza.Builder()
                .size("Medium")
                .dough("Thin")
                .sauce("Tomato")
                .build();

        MenuItem boxThenDip = new TruffleDipDecorator(new EcoPackagingDecorator(basePizza));
        MenuItem dipThenBox = new EcoPackagingDecorator(new TruffleDipDecorator(basePizza));

        assertEquals(basePizza.getPrice() + 3.50, boxThenDip.getPrice(), 0.001);
        assertEquals(boxThenDip.getPrice(), dipThenBox.getPrice(), 0.001);
        assertTrue(boxThenDip.getName().contains("Eco Thermal Box"));
        assertTrue(boxThenDip.getName().contains("Black Truffle Dip"));
    }

    @Test
    void decoratorWrapsPrototypeAndCompositeUniformly() {
        MealComboComposite combo = new MealComboComposite("Mini Combo", 0.0);
        Pizza pizza = new Pizza.Builder().size("Small").dough("Thin").sauce("BBQ").build();
        combo.addItem(pizza);

        MenuItem decoratedCombo = new EcoPackagingDecorator(combo);
        assertEquals(pizza.getPrice() + 1.50, decoratedCombo.getPrice(), 0.001);
    }

    // --- 2. COMPOSITE TESTS ---
    @Test
    void compositeTreatsLeafAndNestedGroupUniformlyViaMenuItem() {
        MenuItem leaf1 = new Pizza.Builder().size("Small").dough("Thin").sauce("Tomato").build();
        MenuItem leaf2 = new Pizza.Builder().size("Medium").dough("Thick").sauce("Cream").build();

        MealComboComposite subGroup = new MealComboComposite("Sub Bundle", 0.0);
        subGroup.addItem(leaf1);

        MealComboComposite mainGroup = new MealComboComposite("Main Party Box", 0.10);
        mainGroup.addItem(subGroup);
        mainGroup.addItem(leaf2);

        double expected = Math.round((leaf1.getPrice() + leaf2.getPrice()) * 0.90 * 100.0) / 100.0;
        assertEquals(expected, mainGroup.getPrice(), 0.001);
        assertTrue(mainGroup.getName().contains("Main Party Box"));
    }

    @Test
    void compositeCanBeAddedDirectlyToOrder() {
        MealComboComposite combo = new MealComboComposite("Lunch Set", 0.0);
        Pizza pizza = new Pizza.Builder().size("Medium").dough("Thin").sauce("Tomato").build();
        combo.addItem(pizza);

        Order order = new Order(99);
        order.add(combo);

        assertEquals(1, order.getItemCount());
        assertEquals(pizza.getPrice(), order.getTotal(), 0.001);
    }

    // --- 3. FACADE TESTS ---
    @Test
    void facadeThrowsExceptionOnEmptyOrder() {
        OrderCheckoutFacade facade = new OrderCheckoutFacade();
        Order emptyOrder = new Order(100);

        assertThrows(IllegalStateException.class, () -> facade.processCheckout(emptyOrder, false));
    }

    @Test
    void facadeProcessesCheckoutRegistersRevenueAndIssuesNextOrder() {
        Restaurant restaurant = Restaurant.getInstance();
        int initialCompleted = restaurant.getCompletedOrders();
        double initialRevenue = restaurant.getRevenue();

        Order order = new Order(restaurant.nextOrderNumber());
        Pizza pizza = new Pizza.Builder().size("Large").dough("Thick").sauce("Tomato").build();
        order.add(pizza);

        OrderCheckoutFacade facade = new OrderCheckoutFacade();
        Order nextOrder = facade.processCheckout(order, false); // BikeDelivery adds $1.50

        assertEquals(initialCompleted + 1, restaurant.getCompletedOrders());
        assertEquals(initialRevenue + pizza.getPrice() + 1.50, restaurant.getRevenue(), 0.001);
        assertNotEquals(order.getNumber(), nextOrder.getNumber());
    }
}