package abstractfactory;

/** Concrete factory: the Italian family of dishes. */
public class ItalianCuisineFactory implements CuisineFactory {

    @Override
    public String getCuisineName() {
        return "Italian";
    }

    @Override
    public MainDish createMainDish() {
        return new Carbonara();
    }

    @Override
    public Drink createDrink() {
        return new Limonata();
    }

    @Override
    public Dessert createDessert() {
        return new Tiramisu();
    }

    // ----- Concrete products -----
    public static class Carbonara implements MainDish {
        @Override public String getName() { return "Spaghetti Carbonara"; }
        @Override public double getPrice() { return 9.00; }
    }

    public static class Limonata implements Drink {
        @Override public String getName() { return "Italian Limonata"; }
        @Override public double getPrice() { return 2.50; }
    }

    public static class Tiramisu implements Dessert {
        @Override public String getName() { return "Tiramisu"; }
        @Override public double getPrice() { return 4.00; }
    }
}
