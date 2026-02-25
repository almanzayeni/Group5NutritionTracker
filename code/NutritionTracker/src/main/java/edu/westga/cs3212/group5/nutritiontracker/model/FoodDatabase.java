package edu.westga.cs3212.group5.nutritiontracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton in-memory database of all known FoodItems.
 * Acts as the central store that search queries run against.
 *
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodDatabase {

    private static FoodDatabase instance;
    private final List<FoodItem> foods = new ArrayList<>();

    /**
     * Returns the single shared instance, creating it on first call.
     *
     * @return the FoodDatabase singleton
     */
    public static FoodDatabase getInstance() {
        if (instance == null) {
            instance = new FoodDatabase();
        }
        return instance;
    }

    private FoodDatabase() {
        this.seedSampleData();
    }

    /**
     * Adds a food item to the database.
     *
     * @precondition food != null
     * @param food the item to add
     * @throws IllegalArgumentException if food is null
     */
    public void addFood(FoodItem food) {
        if (food == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }
        this.foods.add(food);
    }

    /**
     * Returns an unmodifiable view of every item in the database.
     *
     * @return all foods
     */
    public List<FoodItem> getAllFoods() {
        return Collections.unmodifiableList(this.foods);
    }

    /**
     * Returns all foods whose description contains query
     * (case-insensitive). An empty or null query returns all foods.
     *
     * @param query the search string
     * @return matching foods, never null
     */
    public List<FoodItem> search(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(this.foods);
        }
        String lower = query.toLowerCase();
        return this.foods.stream()
                .filter(f -> f.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /**
     * Returns foods matching query, sorted by the given SortOption.
     *
     * @param query  search string (null/blank = all foods)
     * @param sort   how to order results
     * @return sorted, filtered list
     */
    public List<FoodItem> search(String query, SortOption sort) {
        List<FoodItem> results = this.search(query);
        if (sort == null) {
            return results;
        }
        switch (sort) {
            case CALORIES_ASC  -> results.sort((a, b) -> Double.compare(a.getCalories(), b.getCalories()));
            case CALORIES_DESC -> results.sort((a, b) -> Double.compare(b.getCalories(), a.getCalories()));
            case NAME_ASC      -> results.sort((a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));
            case NAME_DESC     -> results.sort((a, b) -> b.getDescription().compareToIgnoreCase(a.getDescription()));
        }
        return results;
    }


    public enum SortOption {
        NAME_ASC("Name (A-Z)"),
        NAME_DESC("Name (Z-A)"),
        CALORIES_ASC("Calories (Low to High)"),
        CALORIES_DESC("Calories (High to Low)");

        private final String label;

        SortOption(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Pre-populates the database with common foods so the search feature works
     * immediately without the user needing to add items first.
     */
    private void seedSampleData() {
        // BaseFood(description, quantityCategory, portionSize, calories, protein, fat, sugar, carbs, sodium)

        this.foods.add(new BaseFood("Apple (medium)",     QuantityCategory.QUANTITY, 1,   95,  0.5,  0.3, 19.0, 25.0,   2.0));
        this.foods.add(new BaseFood("Banana (medium)",    QuantityCategory.QUANTITY, 1,  105,  1.3,  0.4, 14.4, 27.0,   1.0));
        this.foods.add(new BaseFood("Egg (large)",        QuantityCategory.QUANTITY, 1,   72,  6.3,  5.0,  0.2,  0.4,  71.0));
        this.foods.add(new BaseFood("White Bread (slice)",QuantityCategory.QUANTITY, 1,   79,  2.7,  1.0,  1.4, 15.0, 142.0));
        this.foods.add(new BaseFood("Orange (medium)",    QuantityCategory.QUANTITY, 1,   62,  1.2,  0.2, 12.2, 15.4,   0.0));

        this.foods.add(new BaseFood("Chicken Breast",     QuantityCategory.WEIGHT, 100, 165, 31.0,  3.6,  0.0,  0.0,  74.0));
        this.foods.add(new BaseFood("Salmon (Atlantic)",  QuantityCategory.WEIGHT, 100, 208, 20.4, 13.4,  0.0,  0.0,  59.0));
        this.foods.add(new BaseFood("Broccoli",           QuantityCategory.WEIGHT, 100,  34,  2.8,  0.4,  1.7,  6.6,  33.0));
        this.foods.add(new BaseFood("Ground Beef (lean)", QuantityCategory.WEIGHT, 100, 215, 26.1, 12.0,  0.0,  0.0,  75.0));
        this.foods.add(new BaseFood("Sweet Potato",       QuantityCategory.WEIGHT, 100,  86,  1.6,  0.1,  4.2, 20.1,  55.0));
        this.foods.add(new BaseFood("Cheddar Cheese",     QuantityCategory.WEIGHT, 100, 403, 25.0, 33.0,  0.5,  1.3, 621.0));
        this.foods.add(new BaseFood("Almonds",            QuantityCategory.WEIGHT, 100, 579, 21.2, 49.9,  4.4, 21.6,   1.0));
        this.foods.add(new BaseFood("White Rice (cooked)",QuantityCategory.WEIGHT, 100, 130,  2.7,  0.3,  0.0, 28.2,   1.0));
        this.foods.add(new BaseFood("Avocado",            QuantityCategory.WEIGHT, 100, 160,  2.0, 14.7,  0.7,  8.5,   7.0));
        this.foods.add(new BaseFood("Tuna (canned)",      QuantityCategory.WEIGHT, 100, 116, 25.5,  0.8,  0.0,  0.0, 396.0));

        this.foods.add(new BaseFood("Whole Milk (1 cup)",       QuantityCategory.SERVING, 1, 149,  8.0,  8.0, 12.3, 11.7, 105.0));
        this.foods.add(new BaseFood("Orange Juice (1 cup)",     QuantityCategory.SERVING, 1, 112,  1.7,  0.5, 20.8, 25.8,   2.0));
        this.foods.add(new BaseFood("Greek Yogurt (3/4 cup)",   QuantityCategory.SERVING, 1, 100, 17.3,  0.7,  6.0,  6.0,  36.0));
        this.foods.add(new BaseFood("Oatmeal (1 cup cooked)",   QuantityCategory.SERVING, 1, 158,  5.9,  3.2,  0.6, 27.4,   9.0));
        this.foods.add(new BaseFood("Brown Rice (1 cup cooked)",QuantityCategory.SERVING, 1, 216,  5.0,  1.8,  0.7, 44.8,  10.0));
        this.foods.add(new BaseFood("Pasta (1 cup cooked)",     QuantityCategory.SERVING, 1, 220,  8.1,  1.3,  0.6, 43.2,   1.0));
        this.foods.add(new BaseFood("Peanut Butter (2 tbsp)",   QuantityCategory.SERVING, 1, 188,  8.0, 16.0,  3.4,  6.9, 147.0));
        this.foods.add(new BaseFood("Olive Oil (1 tbsp)",       QuantityCategory.SERVING, 1, 119,  0.0, 13.5,  0.0,  0.0,   0.0));
        this.foods.add(new BaseFood("Black Beans (1/2 cup)",    QuantityCategory.SERVING, 1, 114,  7.6,  0.5,  0.3, 20.4, 204.0));
        this.foods.add(new BaseFood("Cottage Cheese (1/2 cup)", QuantityCategory.SERVING, 1, 110, 12.5,  5.0,  3.0,  3.5, 380.0));
    }
}
