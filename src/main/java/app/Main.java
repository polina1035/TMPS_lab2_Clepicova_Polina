package app;

import abstractfactory.CuisineFactory;
import abstractfactory.ItalianCuisineFactory;
import abstractfactory.JapaneseCuisineFactory;
import builder.Pizza;
import composite.MealComboComposite;
import decorator.EcoPackagingDecorator;
import decorator.TruffleDipDecorator;
import facade.OrderCheckoutFacade;
import model.MenuItem;
import model.Order;
import prototype.PizzaMenu;
import singleton.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    private static final List<String> SIZES = Arrays.asList("Small", "Medium", "Large");
    private static final List<String> DOUGHS = Arrays.asList("Thin", "Thick");
    private static final List<String> SAUCES = Arrays.asList("Tomato", "Cream", "BBQ");
    private static final List<String> TOPPINGS =
            Arrays.asList("Pepperoni", "Mushrooms", "Olives", "Ham", "Pineapple");

    private static final PizzaMenu pizzaMenu = new PizzaMenu();
    private static final OrderCheckoutFacade checkoutFacade = new OrderCheckoutFacade();
    private static Order currentOrder;

    public static void main(String[] args) {
        Restaurant restaurant = Restaurant.getInstance();
        currentOrder = new Order(restaurant.nextOrderNumber());

        System.out.println("Welcome to " + restaurant.getName() + "!");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("");
            if (choice == null) {
                break;
            }
            switch (choice) {
                case "1": buildOwnPizza(); break;
                case "2": orderSignaturePizza(); break;
                case "3": orderCombo(); break;
                case "4": orderPartyBoxComposite(); break;
                case "5": currentOrder.print(); break;
                case "6": checkoutWithFacade(); break;
                case "7": showStatistics(); break;
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
        System.out.println(" 1. Build your own pizza [Builder + Decorator]");
        System.out.println(" 2. Order a signature pizza [Prototype + Decorator]");
        System.out.println(" 3. Order a combo meal [Abstract Factory]");
        System.out.println(" 4. Order a Party Box [Composite + Decorator]");
        System.out.println(" 5. Show my order");
        System.out.println(" 6. Checkout and delivery [Facade]");
        System.out.println(" 7. Restaurant statistics [Singleton]");
        System.out.println(" 0. Exit");
        System.out.print("Your choice: ");
    }

    private static MenuItem applyDecoratorsInteractive(MenuItem item) {
        String dip = prompt("Add Black Truffle Dip (+$2.00)? [Decorator] (y/n): ");
        if (dip != null && dip.equalsIgnoreCase("y")) {
            item = new TruffleDipDecorator(item);
        }
        String box = prompt("Add Eco Thermal Box (+$1.50)? [Decorator] (y/n): ");
        if (box != null && box.equalsIgnoreCase("y")) {
            item = new EcoPackagingDecorator(item);
        }
        return item;
    }

    private static void buildOwnPizza() {
        System.out.println("\n--- Build your own pizza  [pattern: BUILDER] ---");

        int size = choose("Select size:", SIZES);
        if (size < 0) return;
        int dough = choose("Select dough:", DOUGHS);
        if (dough < 0) return;
        int sauce = choose("Select sauce:", SAUCES);
        if (sauce < 0) return;

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
                } catch (NumberFormatException ignored) {}
            }
        }

        String cheese = prompt("Extra cheese? (y/n): ");
        builder.extraCheese(cheese != null && cheese.equalsIgnoreCase("y"));

        MenuItem pizza = applyDecoratorsInteractive(builder.build());
        currentOrder.add(pizza);
        System.out.println("Added to your order: " + pizza);
    }

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

        Pizza pizza = pizzaMenu.create(name);

        int extra = choose("Add an extra topping to your copy? (choose 0 to skip)", TOPPINGS, true);
        if (extra >= 0) {
            pizza.addTopping(TOPPINGS.get(extra));
            pizza.setName(name + " (customised)");
        }

        MenuItem finalItem = applyDecoratorsInteractive(pizza);
        currentOrder.add(finalItem);
        System.out.println("Added to your order: " + finalItem);
        System.out.println("The recipe on the menu is unchanged: " + pizzaMenu.create(name));
    }

    private static void orderCombo() {
        System.out.println("\n--- Combo meal  [pattern: ABSTRACT FACTORY] ---");

        int cuisine = choose("Choose a cuisine:", Arrays.asList("Italian", "Japanese"));
        if (cuisine < 0) return;

        CuisineFactory factory = (cuisine == 0)
                ? new ItalianCuisineFactory()
                : new JapaneseCuisineFactory();

        CuisineFactory.MainDish main = factory.createMainDish();
        CuisineFactory.Drink drink = factory.createDrink();
        CuisineFactory.Dessert dessert = factory.createDessert();

        currentOrder.add(main);
        currentOrder.add(drink);
        currentOrder.add(dessert);

        System.out.println(factory.getCuisineName() + " combo added to your order.");
    }

    // =====================================================================
    // COMPOSITE + DECORATOR: unified group bundle treated as one MenuItem
    // =====================================================================
    private static void orderPartyBoxComposite() {
        System.out.println("\n--- Party Box  [pattern: COMPOSITE + DECORATOR] ---");

        MealComboComposite partyBox = new MealComboComposite("Bella Vita Party Box (10% off)", 0.10);

        // Leaf 1: Signature Pizza (Prototype)
        Pizza margherita = pizzaMenu.create("Margherita");
        // Leaf 2: Decorated Signature Pizza (Prototype + Decorator)
        MenuItem decoratedPepperoni = new TruffleDipDecorator(pizzaMenu.create("Pepperoni"));
        // Leaf 3: Drink from Abstract Factory
        CuisineFactory.Drink drink = new ItalianCuisineFactory().createDrink();

        partyBox.addItem(margherita);
        partyBox.addItem(decoratedPepperoni);
        partyBox.addItem(drink);

        // Wrap the entire composite in Eco Packaging (Decorator wrapping Composite)
        MenuItem packagedPartyBox = new EcoPackagingDecorator(partyBox);

        currentOrder.add(packagedPartyBox);
        System.out.println("Added unified composite bundle to order:");
        System.out.println("  " + packagedPartyBox);
    }

    // =====================================================================
    // FACADE: coordinates validation, Factory Method delivery, and Singleton
    // =====================================================================
    private static void checkoutWithFacade() {
        System.out.println("\n--- Checkout  [pattern: FACADE] ---");

        if (currentOrder.isEmpty()) {
            System.out.println("Your order is empty. Add something first.");
            return;
        }

        int type = choose("How far do we deliver?",
                Arrays.asList("Nearby, up to 3 km (bicycle)", "Far, more than 3 km (car)"));
        if (type < 0) return;

        currentOrder = checkoutFacade.processCheckout(currentOrder, type == 1);
    }

    private static void showStatistics() {
        System.out.println("\n--- Restaurant statistics  [pattern: SINGLETON] ---");
        Restaurant restaurant = Restaurant.getInstance();
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Completed orders: " + restaurant.getCompletedOrders());
        System.out.printf(Locale.US, "Total revenue: $%.2f%n", restaurant.getRevenue());
    }

    private static String prompt(String text) {
        System.out.print(text);
        return in.hasNextLine() ? in.nextLine().trim() : null;
    }

    private static int choose(String title, List<String> options) {
        return choose(title, options, false);
    }

    private static int choose(String title, List<String> options, boolean allowSkip) {
        System.out.println(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + options.get(i));
        }
        String line = prompt("Your choice: ");
        if (line == null) return -1;
        try {
            int n = Integer.parseInt(line);
            if (n >= 1 && n <= options.size()) return n - 1;
            if (allowSkip && n == 0) return -1;
        } catch (NumberFormatException ignored) {}
        if (!allowSkip) System.out.println("Invalid choice.");
        return -1;
    }
}