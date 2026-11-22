package composite;

import model.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Composite: groups multiple MenuItem leaves (or nested composites) into a single
 * unified MenuItem with an optional bundle discount.
 */
public class MealComboComposite implements MenuItem {
    private final String bundleName;
    private final double discountRate; // e.g., 0.10 for 10% off
    private final List<MenuItem> items = new ArrayList<>();

    public MealComboComposite(String bundleName, double discountRate) {
        this.bundleName = bundleName;
        this.discountRate = discountRate;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public String getName() {
        String contents = items.stream()
                .map(MenuItem::getName)
                .collect(Collectors.joining(", "));
        return String.format("%s [%s]", bundleName, contents);
    }

    @Override
    public double getPrice() {
        double rawSum = items.stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
        return Math.round(rawSum * (1.0 - discountRate) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f", getName(), getPrice());
    }
}