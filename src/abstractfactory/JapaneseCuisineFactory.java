package abstractfactory;

/** Concrete factory: the Japanese family of dishes. */
public class JapaneseCuisineFactory implements CuisineFactory {

    @Override
    public String getCuisineName() {
        return "Japanese";
    }

    @Override
    public MainDish createMainDish() {
        return new SushiSet();
    }

    @Override
    public Drink createDrink() {
        return new GreenTea();
    }

    @Override
    public Dessert createDessert() {
        return new Mochi();
    }

    // ----- Concrete products -----
    public static class SushiSet implements MainDish {
        @Override public String getName() { return "Sushi set"; }
        @Override public double getPrice() { return 12.00; }
    }

    public static class GreenTea implements Drink {
        @Override public String getName() { return "Green tea"; }
        @Override public double getPrice() { return 2.00; }
    }

    public static class Mochi implements Dessert {
        @Override public String getName() { return "Mochi"; }
        @Override public double getPrice() { return 3.50; }
    }
}
