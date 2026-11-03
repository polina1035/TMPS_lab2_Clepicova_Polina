package app;

import abstractfactory.CuisineFactory;
import abstractfactory.ItalianCuisineFactory;
import abstractfactory.JapaneseCuisineFactory;
import builder.Pizza;
import factorymethod.BikeDelivery;
import factorymethod.CarDelivery;
import factorymethod.DeliveryService;
import model.Order;
import prototype.PizzaMenu;
import singleton.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Console ordering system of "Bella Vita Pizzeria".
 *
 * Where the patterns are used:
 *   Singleton        - Restaurant (order numbers, revenue)
 *   Builder          - "Build your own pizza"
 *   Prototype        - "Signature pizza" (clone from the menu and customise)
 *   Abstract Factory - "Combo meal" (consistent family of dishes)
 *   Factory Method   - "Checkout" (choice of the delivery service)
 *
 */
public class Main {

    private static final Scanner in = new Scanner(System.in);

    private static final List<String> SIZES = Arrays.asList("Small", "Medium", "Large");
    private static final List<String> DOUGHS = Arrays.asList("Thin", "Thick");
    private static final List<String> SAUCES = Arrays.asList("Tomato", "Cream", "BBQ");
    private static final List<String> TOPPINGS =
            Arrays.asList("Pepperoni", "Mushrooms", "Olives", "Ham", "Pineapple");

    private static final PizzaMenu pizzaMenu = new PizzaMenu();
    private static Order currentOrder;

    public static void main(String[] args) {
        // Singleton in action: the restaurant gives out order numbers
        Restaurant restaurant = Restaurant.getInstance();
        currentOrder = new Order(restaurant.nextOrderNumber());

        System.out.println("Welcome to " + restaurant.getName() + "!");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("");
            if (choice == null) {
                break; // input closed
            }
            switch (choice) {
                case "1": buildOwnPizza(); break;
                case "2": orderSignaturePizza(); break;
                case "3": orderCombo(); break;
                case "4": currentOrder.print(); break;
                case "5": checkout(); break;
                case "6": showStatistics(); break;
                case "0":
                    running = false;
                    System.out.println("Thank you for visiting. Goodbye!");
                    break;
                default:
                    System.out.println("Unknown option, please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("  BELLA VITA - Order #" + currentOrder.getNumber()
                + " (" + currentOrder.getItemCount() + " items)");
        System.out.println("==========================================");
        System.out.println(" 1. Build your own pizza");
        System.out.println(" 2. Order a signature pizza");
        System.out.println(" 3. Order a combo meal");
        System.out.println(" 4. Show my order");
        System.out.println(" 5. Checkout and delivery");
        System.out.println(" 6. Restaurant statistics");
        System.out.println(" 0. Exit");
        System.out.print("Your choice: ");
    }

    // =====================================================================
    // BUILDER: the customer assembles a pizza step by step
    // =====================================================================
    private static void buildOwnPizza() {
        System.out.println("\n--- Build your own pizza  [pattern: BUILDER] ---");

        int size = choose("Select size:", SIZES);
        if (size < 0) return;
        int dough = choose("Select dough:", DOUGHS);
        if (dough < 0) return;
        int sauce = choose("Select sauce:", SAUCES);
        if (sauce < 0) return;

        // Each answer is one step of the builder
        Pizza.Builder builder = new Pizza.Builder()
                .size(SIZES.get(size))
                .dough(DOUGHS.get(dough))
                .sauce(SAUCES.get(sauce));

        System.out.println("Available toppings:");
        for (int i = 0; i < TOPPINGS.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + TOPPINGS.get(i));
        }
        String line = prompt("Enter topping numbers separated by commas (Enter = none): ");
        if (line != null && !line.isEmpty()) {
            for (String part : line.split(",")) {
                try {
                    int n = Integer.parseInt(part.trim());
                    if (n >= 1 && n <= TOPPINGS.size()) {
                        builder.topping(TOPPINGS.get(n - 1));
                    }
                } catch (NumberFormatException e) {
                    // ignore anything that is not a number
                }
            }
        }

        String cheese = prompt("Extra cheese? (y/n): ");
        builder.extraCheese(cheese != null && cheese.equalsIgnoreCase("y"));

        Pizza pizza = builder.build(); // the final step
        currentOrder.add(pizza);
        System.out.println("Added to your order: " + pizza);
    }

    // =====================================================================
    // PROTOTYPE: clone a signature pizza and customise the copy
    // =====================================================================
    private static void orderSignaturePizza() {
        System.out.println("\n--- Signature pizza  [pattern: PROTOTYPE] ---");

        List<String> names = pizzaMenu.getNames();
        List<String> display = new ArrayList<>();
        for (String name : names) {
            display.add(pizzaMenu.create(name).toString());
        }

        int index = choose("Our signature pizzas:", display);
        if (index < 0) return;
        String name = names.get(index);

        // We get a COPY of the recipe, not the recipe itself
        Pizza pizza = pizzaMenu.create(name);

        int extra = choose("Add an extra topping to your copy? (choose 0 to skip)", TOPPINGS, true);
        if (extra >= 0) {
            pizza.addTopping(TOPPINGS.get(extra));
            pizza.setName(name + " (customised)");
        }

        currentOrder.add(pizza);
        System.out.println("Added to your order: " + pizza);
        System.out.println("The recipe on the menu is unchanged: " + pizzaMenu.create(name));
    }

    // =====================================================================
    // ABSTRACT FACTORY: a consistent combo meal of one cuisine
    // =====================================================================
    private static void orderCombo() {
        System.out.println("\n--- Combo meal  [pattern: ABSTRACT FACTORY] ---");

        int cuisine = choose("Choose a cuisine:", Arrays.asList("Italian", "Japanese"));
        if (cuisine < 0) return;

        // One choice selects a whole family of dishes
        CuisineFactory factory = (cuisine == 0)
                ? new ItalianCuisineFactory()
                : new JapaneseCuisineFactory();

        CuisineFactory.MainDish main = factory.createMainDish();
        CuisineFactory.Drink drink = factory.createDrink();
        CuisineFactory.Dessert dessert = factory.createDessert();

        currentOrder.add(main);
        currentOrder.add(drink);
        currentOrder.add(dessert);

        System.out.println(factory.getCuisineName() + " combo added to your order:");
        System.out.printf(Locale.US, "  - %s ($%.2f)%n", main.getName(), main.getPrice());
        System.out.printf(Locale.US, "  - %s ($%.2f)%n", drink.getName(), drink.getPrice());
        System.out.printf(Locale.US, "  - %s ($%.2f)%n", dessert.getName(), dessert.getPrice());
    }

    // =====================================================================
    // FACTORY METHOD: the chosen delivery service creates its own courier
    // =====================================================================
    private static void checkout() {
        System.out.println("\n--- Checkout  [pattern: FACTORY METHOD] ---");

        if (currentOrder.isEmpty()) {
            System.out.println("Your order is empty. Add something first.");
            return;
        }

        int type = choose("How far do we deliver?",
                Arrays.asList("Nearby, up to 3 km (bicycle)", "Far, more than 3 km (car)"));
        if (type < 0) return;

        // The client works with the abstract DeliveryService only
        DeliveryService service = (type == 0) ? new BikeDelivery() : new CarDelivery();
        service.dispatch(currentOrder);

        System.out.println();
        currentOrder.print();

        // Singleton in action: the single cash register records the sale
        Restaurant restaurant = Restaurant.getInstance();
        restaurant.registerSale(currentOrder.getTotal());
        System.out.println("\nPayment accepted. Thank you!");

        // Start a new order; the single restaurant gives it a unique number
        currentOrder = new Order(restaurant.nextOrderNumber());
    }

    // =====================================================================
    // SINGLETON: the same restaurant object accumulates all the statistics
    // =====================================================================
    private static void showStatistics() {
        System.out.println("\n--- Restaurant statistics  [pattern: SINGLETON] ---");
        Restaurant restaurant = Restaurant.getInstance(); // same object as in main()
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Completed orders: " + restaurant.getCompletedOrders());
        System.out.printf(Locale.US, "Total revenue: $%.2f%n", restaurant.getRevenue());
    }

    // =====================================================================
    // Input helpers
    // =====================================================================

    // Reads a line; returns null when the input stream is closed
    private static String prompt(String text) {
        System.out.print(text);
        return in.hasNextLine() ? in.nextLine().trim() : null;
    }

    private static int choose(String title, List<String> options) {
        return choose(title, options, false);
    }

    // Shows a numbered list and returns the chosen index (or -1 if invalid / skipped)
    private static int choose(String title, List<String> options, boolean allowSkip) {
        System.out.println(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + options.get(i));
        }
        String line = prompt("Your choice: ");
        if (line == null) {
            return -1;
        }
        try {
            int n = Integer.parseInt(line);
            if (n >= 1 && n <= options.size()) {
                return n - 1;
            }
            if (allowSkip && n == 0) {
                return -1;
            }
        } catch (NumberFormatException e) {
            // fall through to the message below
        }
        if (!allowSkip) {
            System.out.println("Invalid choice.");
        }
        return -1;
    }
}
