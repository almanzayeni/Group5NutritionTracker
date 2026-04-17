package edu.westga.cs3212.group5.nutritiontracker.model;

/**
 * Sort options for food search results.
 *
 * @author Yeni Almanza
 * @version Spring 2026
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