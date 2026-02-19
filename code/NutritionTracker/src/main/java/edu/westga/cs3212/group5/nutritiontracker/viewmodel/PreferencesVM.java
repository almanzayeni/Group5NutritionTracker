package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.UserPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class PreferencesVM.
 * ViewModel for the Preferences page. Uses {@link DoubleProperty} for all
 * numeric macro fields so that no string-to-double casting is needed in the
 * controller.
 *
 * @author Yeni Almanza
 * @version spring 2026
 */
public class PreferencesVM {

    private final DoubleProperty calorie  = new SimpleDoubleProperty(0.0);
    private final DoubleProperty protein  = new SimpleDoubleProperty(0.0);
    private final DoubleProperty fat      = new SimpleDoubleProperty(0.0);
    private final DoubleProperty sugar    = new SimpleDoubleProperty(0.0);
    private final DoubleProperty sodium   = new SimpleDoubleProperty(0.0);
    private final DoubleProperty carbs    = new SimpleDoubleProperty(0.0);
    private final StringProperty warning  = new SimpleStringProperty("");

    /**
     * Gets the calorie property.
     *
     * @return the calorie property
     */
    public DoubleProperty calorieProperty() {
        return this.calorie;
    }

    /**
     * Gets the protein property.
     *
     * @return the protein property
     */
    public DoubleProperty proteinProperty() {
        return this.protein;
    }

    /**
     * Gets the fat property.
     *
     * @return the fat property
     */
    public DoubleProperty fatProperty() {
        return this.fat;
    }

    /**
     * Gets the sugar property.
     *
     * @return the sugar property
     */
    public DoubleProperty sugarProperty() {
        return this.sugar;
    }

    /**
     * Gets the sodium property.
     *
     * @return the sodium property
     */
    public DoubleProperty sodiumProperty() {
        return this.sodium;
    }

    /**
     * Gets the carbs property.
     *
     * @return the carbs property
     */
    public DoubleProperty carbsProperty() {
        return this.carbs;
    }

    /**
     * Gets the warning property.
     *
     * @return the warning property
     */
    public StringProperty warningProperty() {
        return this.warning;
    }

    /**
     * Creates a UserPreferences from the current property values.
     *
     * @precondition primaryGoal != null
     *
     * @param primaryGoal    the selected primary goal enum value
     * @param otherGoalsText comma-separated string of additional goals (may be blank)
     * @return a new {@link UserPreferences}, or {@code null} if validation fails
     */
    public UserPreferences createPreferences(PrimaryGoal primaryGoal, String otherGoalsText) {
        try {
            List<String> otherGoals = (otherGoalsText == null || otherGoalsText.isBlank())
                    ? List.of()
                    : Arrays.stream(otherGoalsText.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

            UserPreferences prefs = new UserPreferences(
                    primaryGoal,
                    this.calorie.get(),
                    this.protein.get(),
                    this.fat.get(),
                    this.sugar.get(),
                    this.sodium.get(),
                    this.carbs.get(),
                    otherGoals
            );

            this.warning.set("");
            return prefs;

        } catch (Exception e) {
            this.warning.set(e.getMessage());
            return null;
        }
    }
}