package prototype;

import builder.Pizza;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PROTOTYPE registry: the restaurant's menu of signature pizzas.
 *
 * Real-world reasoning: signature recipes are prepared once. When a customer
 * orders one, we do not rebuild it from scratch - we take a COPY of the recipe
 * card. The customer can modify the copy, the recipe stays untouched.
 *
 * (The recipes themselves are created with the Builder - patterns cooperate.)
 */
public class PizzaMenu {

    private final Map<String, Pizza> prototypes = new LinkedHashMap<>();

    public PizzaMenu() {
        register(new Pizza.Builder()
                .name("Margherita")
                .topping("Mozzarella").topping("Basil")
                .build());

        register(new Pizza.Builder()
                .name("Pepperoni")
                .topping("Pepperoni").topping("Mozzarella")
                .build());

        register(new Pizza.Builder()
                .name("Hawaiian")
                .dough("Thick")
                .topping("Ham").topping("Pineapple").topping("Mozzarella")
                .build());
    }

    private void register(Pizza pizza) {
        prototypes.put(pizza.getName(), pizza);
    }

    public List<String> getNames() {
        return new ArrayList<>(prototypes.keySet());
    }

    // Returns a COPY of the recipe, never the original
    public Pizza create(String name) {
        Pizza prototype = prototypes.get(name);
        return prototype == null ? null : prototype.copy();
    }
}
