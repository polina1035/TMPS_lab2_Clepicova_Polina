package singleton;

/**
 * SINGLETON: the restaurant itself.
 *
 * Real-world reasoning: there is only ONE restaurant. It must have a single
 * order counter (so two orders never get the same number) and a single cash
 * register (so the revenue is counted in one place).
 */
public class Restaurant {

    // The only instance
    private static Restaurant instance;

    private final String name = "Bella Vita Pizzeria";
    private int orderCounter = 0;
    private int completedOrders = 0;
    private double revenue = 0;

    // Private constructor: nobody can open a "second restaurant"
    private Restaurant() {
        System.out.println("[Singleton] The restaurant instance is created (this happens only once).");
    }

    // Global access point (lazy and thread-safe)
    public static synchronized Restaurant getInstance() {
        if (instance == null) {
            instance = new Restaurant();
        }
        return instance;
    }

    // Hands out unique order numbers
    public synchronized int nextOrderNumber() {
        return ++orderCounter;
    }

    // Called when an order is paid
    public synchronized void registerSale(double amount) {
        completedOrders++;
        revenue += amount;
    }

    public String getName() {
        return name;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public double getRevenue() {
        return revenue;
    }
}
