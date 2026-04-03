package edu.westga.cs3212.group5.nutritiontracker.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton database of all known FoodItems, backed by a JSON file.
 *
 * File format: one JSON object per line (NDJSON). Each line carries a "type"
 * discriminator ("base" or "composite") so Jackson can deserialize back to
 * the correct concrete class via the @JsonTypeInfo on FoodItem.
 *
 * Place food_database.json in the project root before first run.
 * The file is the source of truth — nothing is held in memory between calls.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodDatabase {

    public static final String DATABASE_FILE = "food_database.json";

    private static FoodDatabase instance;
    private final String filePath;
    private final ObjectMapper objectMapper;


    /**
     * Returns the single shared instance, creating it on first call.
     *
     * @return the FoodDatabase singleton
     */
    public static FoodDatabase getInstance() {
        if (instance == null) {
            instance = new FoodDatabase(DATABASE_FILE);
        }
        return instance;
    }

    /**
     * Package-private constructor for testing — accepts a custom file path.
     *
     * @param filePath path to the backing NDJSON file
     */
    FoodDatabase(String filePath) {
        this.filePath = filePath;
        this.objectMapper = JsonMapperFactory.create();
    }

    /**
     * Appends a food item as a JSON line to the backing file.
     *
     * @precondition food != null
     * @param food the item to persist
     * @throws IllegalArgumentException if food is null
     * @throws RuntimeException if the file cannot be written
     */
    public void addFood(FoodItem food) {
        if (food == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }
        try {
            String line = this.objectMapper.writeValueAsString(food);
            Files.write(Paths.get(this.filePath),
                    (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save food to database file: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all foods loaded from the backing file.
     *
     * @return all foods, never null
     */
    public List<FoodItem> getAllFoods() {
        return this.loadAll();
    }

    /**
     * Returns all foods whose description contains {@code query}
     * (case-insensitive). A null or blank query returns all foods.
     *
     * @param query the search string
     * @return matching foods, never null
     */
    public List<FoodItem> search(String query) {
        List<FoodItem> all = this.loadAll();
        if (query == null || query.isBlank()) {
            return all;
        }
        String lower = query.toLowerCase();
        return all.stream()
                .filter(f -> f.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /**
     * Returns foods matching {@code query}, sorted by the given {@link SortOption}.
     *
     * @param query search string (null/blank = all foods)
     * @param sort  how to order results
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

    /**
     * Sort options for search results.
     */
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
     * Reads every line from the backing file and deserializes it as a FoodItem.
     * Lines that fail to parse are skipped with a warning printed to stderr.
     */
    private List<FoodItem> loadAll() {
        List<FoodItem> foods = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(this.filePath));
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    foods.add(this.objectMapper.readValue(line, FoodItem.class));
                } catch (IOException e) {
                    System.err.println("Skipping unreadable line in food database: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read food database file: " + e.getMessage());
        }
        return foods;
    }
}