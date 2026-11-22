package abstractfactory;

import model.MenuItem;

/**
 * ABSTRACT FACTORY: a kitchen that prepares a whole "combo meal" in one style.
 *
 * Real-world reasoning: a combo must be consistent - spaghetti goes with
 * tiramisu, sushi goes with green tea. You should never get sushi with
 * tiramisu. Each cuisine factory produces a matching FAMILY of dishes.
 */
public interface CuisineFactory {

    String getCuisineName();

    MainDish createMainDish();

    Drink createDrink();

    Dessert createDessert();

    // Abstract products (all of them are menu items)
    interface MainDish extends MenuItem { }

    interface Drink extends MenuItem { }

    interface Dessert extends MenuItem { }
}
