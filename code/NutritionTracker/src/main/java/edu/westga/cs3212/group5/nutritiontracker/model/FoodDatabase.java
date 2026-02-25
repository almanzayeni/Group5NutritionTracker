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
        this.foods.add(new BaseFood("Apple",          QuantityCategory.QUANTITY, 1,   95,  0.5,  0.3,  19.0, 25.0,   2.0));
        this.foods.add(new BaseFood("Banana",         QuantityCategory.QUANTITY, 1,  105,  1.3,  0.4,  14.0, 27.0,   1.0));
        this.foods.add(new BaseFood("Chicken Breast", QuantityCategory.WEIGHT,   100, 165, 31.0,  3.6,   0.0,  0.0,  74.0));
        this.foods.add(new BaseFood("Brown Rice",     QuantityCategory.SERVING,  1,  216,  5.0,  1.8,   0.0, 45.0,  10.0));
        this.foods.add(new BaseFood("Egg",            QuantityCategory.QUANTITY, 1,   78,  6.0,  5.0,   0.1,  0.6,  62.0));
        this.foods.add(new BaseFood("Whole Milk",     QuantityCategory.SERVING,  1,  149,  8.0,  8.0,  12.0, 12.0, 105.0));
        this.foods.add(new BaseFood("Oatmeal",        QuantityCategory.SERVING,  1,  158,  6.0,  3.0,   1.0, 27.0,   9.0));
        this.foods.add(new BaseFood("Salmon",         QuantityCategory.WEIGHT,   100, 208, 20.0, 13.0,   0.0,  0.0,  59.0));
        this.foods.add(new BaseFood("Broccoli",       QuantityCategory.WEIGHT,   100,  34,  2.8,  0.4,   1.7,  7.0,  33.0));
        this.foods.add(new BaseFood("Cheddar Cheese", QuantityCategory.WEIGHT,   28,  113,  7.0,  9.0,   0.1,  0.4, 174.0));
        this.foods.add(new BaseFood("Orange Juice",   QuantityCategory.SERVING,  1,  112,  1.7,  0.5,  20.8, 25.8,   2.0));
        this.foods.add(new BaseFood("Greek Yogurt",   QuantityCategory.SERVING,  1,  100, 17.0,  0.7,   6.0,  6.0,  36.0));
        this.foods.add(new BaseFood("Almonds",        QuantityCategory.WEIGHT,   28,  164,  6.0, 14.0,   1.2,  6.0,   0.0));
        this.foods.add(new BaseFood("White Bread",    QuantityCategory.QUANTITY, 1,   79,  2.7,  1.0,   1.4, 15.0, 142.0));
        this.foods.add(new BaseFood("Pasta",          QuantityCategory.SERVING,  1,  220,  8.0,  1.3,   0.6, 43.0,   1.0));
    }
}
