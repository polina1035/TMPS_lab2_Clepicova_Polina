package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** A customer's order (the "shopping cart"). */
public class Order {

    private final int number;
    private final List<MenuItem> items = new ArrayList<>();
    private double deliveryFee = 0;

    public Order(int number) {
        this.number = number;
    }

    public void add(MenuItem item) {
        items.add(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getNumber() {
        return number;
    }

    public int getItemCount() {
        return items.size();
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getItemsTotal() {
        double sum = 0;
        for (MenuItem item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    public double getTotal() {
        return getItemsTotal() + deliveryFee;
    }

    /** Prints the order like a receipt. */
    public void print() {
        System.out.println("  ----- ORDER #" + number + " -----");
        if (items.isEmpty()) {
            System.out.println("  (empty)");
        }
        for (MenuItem item : items) {
            System.out.printf(Locale.US, "  - %s  ($%.2f)%n", item.getDescription(), item.getPrice());
        }
        System.out.printf(Locale.US, "  Items:    $%.2f%n", getItemsTotal());
        System.out.printf(Locale.US, "  Delivery: $%.2f%n", deliveryFee);
        System.out.printf(Locale.US, "  TOTAL:    $%.2f%n", getTotal());
    }
}
