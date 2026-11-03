package builder;

import model.MenuItem;
import prototype.Prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A pizza.
 *
 * BUILDER: a pizza has many options (size, dough, sauce, toppings, extra
 * cheese), so the customer "builds" it step by step with Pizza.Builder.
 *
 * PROTOTYPE: a pizza can also copy itself (copy()), so signature pizzas from
 * the menu can be cloned and then customised.
 */
public class Pizza implements MenuItem, Prototype<Pizza> {

    private String name;
    private final String size;
    private final String dough;
    private final String sauce;
    private final List<String> toppings;
    private final boolean extraCheese;

    // Used by the Builder
    private Pizza(Builder b) {
        this.name = b.name;
        this.size = b.size;
        this.dough = b.dough;
        this.sauce = b.sauce;
        this.toppings = new ArrayList<>(b.toppings);
        this.extraCheese = b.extraCheese;
    }

    // Copy constructor used by the Prototype
    private Pizza(Pizza other) {
        this.name = other.name;
        this.size = other.size;
        this.dough = other.dough;
        this.sauce = other.sauce;
        // DEEP copy: the list is copied too, otherwise the clone and the
        // original would share (and change) the same toppings list
        this.toppings = new ArrayList<>(other.toppings);
        this.extraCheese = other.extraCheese;
    }

    // ----- Prototype -----
    @Override
    public Pizza copy() {
        return new Pizza(this);
    }

    // Methods used to customise a clone
    public void addTopping(String topping) {
        toppings.add(topping);
    }

    public void setName(String name) {
        this.name = name;
    }

    // ----- MenuItem -----
    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        double price;
        switch (size) {
            case "Small": price = 6.0; break;
            case "Large": price = 10.0; break;
            default:      price = 8.0;
        }
        if (dough.equals("Thick")) {
            price += 1.0;
        }
        price += toppings.size() * 1.0;
        if (extraCheese) {
            price += 1.5;
        }
        return price;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" [").append(size).append(", ").append(dough).append(" dough, ")
          .append(sauce).append(" sauce");
        if (!toppings.isEmpty()) {
            sb.append(", toppings: ").append(String.join(", ", toppings));
        }
        if (extraCheese) {
            sb.append(", extra cheese");
        }
        return sb.append("]").toString();
    }

    @Override
    public String toString() {
        return getDescription() + String.format(Locale.US, " - $%.2f", getPrice());
    }

    // =================================================================
    // BUILDER
    // =================================================================
    public static class Builder {
        // Default values: a plain medium pizza
        private String name = "Custom pizza";
        private String size = "Medium";
        private String dough = "Thin";
        private String sauce = "Tomato";
        private final List<String> toppings = new ArrayList<>();
        private boolean extraCheese = false;

        // Every step returns "this" to allow chaining
        public Builder name(String name) { this.name = name; return this; }
        public Builder size(String size) { this.size = size; return this; }
        public Builder dough(String dough) { this.dough = dough; return this; }
        public Builder sauce(String sauce) { this.sauce = sauce; return this; }
        public Builder topping(String topping) { this.toppings.add(topping); return this; }
        public Builder extraCheese(boolean extraCheese) { this.extraCheese = extraCheese; return this; }

        // Final step: produce the finished pizza
        public Pizza build() {
            return new Pizza(this);
        }
    }
}
