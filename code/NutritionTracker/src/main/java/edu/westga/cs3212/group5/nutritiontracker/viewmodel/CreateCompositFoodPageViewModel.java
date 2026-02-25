package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Create Composite Food page.
 *
 * The actual food-search popup is owned by the controller (it is a UI concern);
 * this ViewModel only cares about the resulting FoodItem once the user
 * has confirmed their choice.
 *
 * @author Group 5
 * @version Spring 2026
 */
public class CreateCompositFoodPageViewModel {


    private final ObservableList<FoodItem> ingredients =
            FXCollections.observableArrayList();

    private final DoubleProperty totalCalories     = new SimpleDoubleProperty(0);
    private final DoubleProperty totalProtein      = new SimpleDoubleProperty(0);
    private final DoubleProperty totalFat          = new SimpleDoubleProperty(0);
    private final DoubleProperty totalSugar        = new SimpleDoubleProperty(0);
    private final DoubleProperty totalCarbohydrates = new SimpleDoubleProperty(0);
    private final DoubleProperty totalSodium       = new SimpleDoubleProperty(0);
    private final StringProperty statusMessage     = new SimpleStringProperty("");

    /**
     * Returns the observable list of selected ingredients.
     * Bind a {@code ListView} to this list so it updates automatically.
     *
     * @return the ingredients list
     */
    public ObservableList<FoodItem> getIngredients() {
        return this.ingredients;
    }

    /** @return total-calories property */
    public DoubleProperty totalCaloriesProperty()      { return this.totalCalories; }
    /** @return total-protein property */
    public DoubleProperty totalProteinProperty()       { return this.totalProtein; }
    /** @return total-fat property */
    public DoubleProperty totalFatProperty()           { return this.totalFat; }
    /** @return total-sugar property */
    public DoubleProperty totalSugarProperty()         { return this.totalSugar; }
    /** @return total-carbohydrates property */
    public DoubleProperty totalCarbohydratesProperty() { return this.totalCarbohydrates; }
    /** @return total-sodium property */
    public DoubleProperty totalSodiumProperty()        { return this.totalSodium; }
    /** @return status / warning message property */
    public StringProperty statusMessageProperty()      { return this.statusMessage; }


    /**
     * Adds food to the ingredient list if it is not already present,
     * then recalculates the nutritional totals.
     *
     * @precondition food != null
     * @param food the food item to add
     * @return {@code true} if added, {@code false} if it was already in the list
     * @throws IllegalArgumentException if food is null
     */
    public boolean addIngredient(FoodItem food) {
        if (food == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }

        boolean alreadyAdded = this.ingredients.stream()
                .anyMatch(f -> f.getDescription().equalsIgnoreCase(food.getDescription()));

        if (alreadyAdded) {
            this.statusMessage.set("\"" + food.getDescription() + "\" is already in the ingredient list.");
            return false;
        }

        this.ingredients.add(food);
        this.recalculateTotals();
        this.statusMessage.set("Added \"" + food.getDescription() + "\" to ingredients.");
        return true;
    }

    /**
     * Removes the ingredient at the given index and recalculates totals.
     *
     * @precondition index >= 0 && index < ingredients.size()
     * @param index the index to remove
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void removeIngredient(int index) {
        if (index < 0 || index >= this.ingredients.size()) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        FoodItem removed = this.ingredients.remove(index);
        this.recalculateTotals();
        this.statusMessage.set("Removed \"" + removed.getDescription() + "\".");
    }

    /**
     * Clears all ingredients and resets totals to zero.
     */
    public void clearIngredients() {
        this.ingredients.clear();
        this.recalculateTotals();
        this.statusMessage.set("");
    }

    /**
     * Sums up all nutritional values from the current ingredient list and
     * updates the observable properties.
     */
    private void recalculateTotals() {
        double cal  = 0, prot = 0, fat  = 0;
        double sug  = 0, carb = 0, sod  = 0;

        for (FoodItem item : this.ingredients) {
            cal  += item.getCalories();
            prot += item.getProtein();
            fat  += item.getFat();
            sug  += item.getSugar();
            carb += item.getCarbohydrates();
            sod  += item.getSodium();
        }

        this.totalCalories.set(cal);
        this.totalProtein.set(prot);
        this.totalFat.set(fat);
        this.totalSugar.set(sug);
        this.totalCarbohydrates.set(carb);
        this.totalSodium.set(sod);
    }
}