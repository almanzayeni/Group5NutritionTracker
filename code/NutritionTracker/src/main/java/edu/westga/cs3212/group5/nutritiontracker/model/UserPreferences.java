package edu.westga.cs3212.group5.nutritiontracker.model;

import java.util.Collections;
import java.util.List;

/**
 * The Class UserPreferences.
 * Represents a user's diet plan preferences and macro goals.
 *
 * The {@code primaryGoal} is one of the known {@link PrimaryGoal} options that
 * the user has selected as their main focus. Additional, free-form goals may be
 * stored in {@code otherGoals}.
 *
 * @author Yeni Almanza
 * @version spring 2026
 */
public class UserPreferences {

    private final PrimaryGoal primaryGoal;
    private final double calorieGoal;
    private final double proteinGoal;
    private final double fatGoal;
    private final double sugarGoal;
    private final double sodiumGoal;
    private final double carbsGoal;
    private final List<String> otherGoals;

    /**
     * Instantiates a new user preferences.
     *
     * @precondition primaryGoal != null
     *               && calorieGoal >= 0
     *               && proteinGoal >= 0
     *               && fatGoal >= 0
     *               && sugarGoal >= 0
     *               && sodiumGoal >= 0
     *               && carbsGoal >= 0
     *
     * @param primaryGoal the user's primary goal — one of the {@link PrimaryGoal}
     *        enum values that the user has chosen as their main focus
     * @param calorieGoal  the daily calorie goal (kcal)
     * @param proteinGoal  the daily protein goal (grams)
     * @param fatGoal      the daily fat goal (grams)
     * @param sugarGoal    the daily sugar goal (grams)
     * @param sodiumGoal   the daily sodium goal (milligrams)
     * @param carbsGoal    the daily carbohydrates goal (grams)
     * @param otherGoals   optional list of additional free-form goals or notes;
     *        a null list is treated as empty, and a defensive copy is stored
     *
     * @throws IllegalArgumentException if primaryGoal is null, or if any numeric
     *         goal is negative
     */
    public UserPreferences(PrimaryGoal primaryGoal, double calorieGoal, double proteinGoal, double fatGoal,
            double sugarGoal, double sodiumGoal, double carbsGoal, List<String> otherGoals) {

        if (primaryGoal == null) {
            throw new IllegalArgumentException("Primary goal cannot be null");
        }
        if (calorieGoal < 0) {
            throw new IllegalArgumentException("Calorie goal cannot be negative");
        }
        if (proteinGoal < 0) {
            throw new IllegalArgumentException("Protein goal cannot be negative");
        }
        if (fatGoal < 0) {
            throw new IllegalArgumentException("Fat goal cannot be negative");
        }
        if (sugarGoal < 0) {
            throw new IllegalArgumentException("Sugar goal cannot be negative");
        }
        if (sodiumGoal < 0) {
            throw new IllegalArgumentException("Sodium goal cannot be negative");
        }
        if (carbsGoal < 0) {
            throw new IllegalArgumentException("Carbohydrates goal cannot be negative");
        }

        this.primaryGoal = primaryGoal;
        this.calorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.fatGoal = fatGoal;
        this.sugarGoal = sugarGoal;
        this.sodiumGoal = sodiumGoal;
        this.carbsGoal = carbsGoal;

        this.otherGoals = otherGoals == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(List.copyOf(otherGoals));
    }

    /**
     * Gets the primary goal.
     *
     * @return the primary goal
     */
    public PrimaryGoal getPrimaryGoal() {
        return this.primaryGoal;
    }

    /**
     * Gets the daily calorie goal (cal).
     *
     * @return the calorie goal
     */
    public double getCalorieGoal() {
        return this.calorieGoal;
    }

    /**
     * Gets the daily protein goal (grams).
     *
     * @return the protein goal
     */
    public double getProteinGoal() {
        return this.proteinGoal;
    }

    /**
     * Gets the daily fat goal (grams).
     *
     * @return the fat goal
     */
    public double getFatGoal() {
        return this.fatGoal;
    }

    /**
     * Gets the daily sugar goal (grams).
     *
     * @return the sugar goal
     */
    public double getSugarGoal() {
        return this.sugarGoal;
    }

    /**
     * Gets the daily sodium goal (milligrams).
     *
     * @return the sodium goal
     */
    public double getSodiumGoal() {
        return this.sodiumGoal;
    }

    /**
     * Gets the daily carbohydrates goal (grams).
     *
     * @return the carbohydrates goal
     */
    public double getCarbsGoal() {
        return this.carbsGoal;
    }

    /**
     * Gets the otheradditional goals.
     *
     * @return an unmodifiable list of additional goals
     */
    public List<String> getOtherGoals() {
        return this.otherGoals;
    }
}