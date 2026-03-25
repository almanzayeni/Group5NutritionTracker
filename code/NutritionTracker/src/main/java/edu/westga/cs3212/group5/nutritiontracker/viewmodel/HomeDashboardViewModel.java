package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.time.LocalDate;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Dashboard VM
 * 
 * @author vfilpo + Emi :)
 * @version Spring 2026
 */
public class HomeDashboardViewModel {
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());
    private final ObservableList<FoodItem> breakfastItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> lunchItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> dinnerItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> snacksItems = FXCollections.observableArrayList();
    
    private final ReadOnlyDoubleWrapper totalCalories = new ReadOnlyDoubleWrapper();
    
    /**
     * HomeDashboard VM Constructor.
     * Binds calorie totals to foods added.
     */
    public HomeDashboardViewModel() {
    	DoubleBinding total = Bindings.createDoubleBinding(this::computeTotalCalories, breakfastItems, lunchItems, dinnerItems, snacksItems);
    	this.totalCalories.bind(total);
    }

    /**
     * Get selected date property.
     * 
     * @return selected date property.
     */
    public ObjectProperty<LocalDate> getSelectedDateProperty() {
        return this.selectedDate;
    }

    /**
     * Get selected date.
     * 
     * @return selected date.
     */
    public LocalDate getSelectedDate() {
        return this.selectedDate.get();
    }

    /**
     * Set selected date.
     * 
     * @param date to set the selected date to.
     */
    public void setSelectedDate(LocalDate date) {
    	this.selectedDate.set(date);
    }
    
    /**
     * Get breakfast food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getBreakfastItems() {
        return this.breakfastItems;
    }

    /**
     * Get lunch food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getLunchItems() {
        return this.lunchItems;
    }

    /**
     * Get dinner food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getDinnerItems() {
        return this.dinnerItems;
    }

    /**
     * Get snack food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getSnacksItems() {
        return this.snacksItems;
    }

    /**
     * Returns a read-only property representing the total calories for the day.
     *
     * @return the read-only total calories property
     */
    public ReadOnlyDoubleProperty totalCaloriesProperty() {
        return this.totalCalories.getReadOnlyProperty();
    }
    
    /**
     * Adds a food item to the breakfast list.
     *
     * @param item the food item to add
     */
    public void addToBreakfast(FoodItem item) {
        this.breakfastItems.add(item);
    }

    /**
     * Adds a food item to the lunch list.
     *
     * @param item the food item to add
     */
    public void addToLunch(FoodItem item) {
        this.lunchItems.add(item);
    }

    /**
     * Adds a food item to the dinner list.
     *
     * @param item the food item to add
     */
    public void addToDinner(FoodItem item) {
        this.dinnerItems.add(item);
    }

    /**
     * Adds a food item to the snacks list.
     *
     * @param item the food item to add
     */
    public void addToSnacks(FoodItem item) {
        this.snacksItems.add(item);
    }

    /**
     * Removes a food item from the breakfast list.
     *
     * @param item the food item to remove
     */
    public void removeFromBreakfast(FoodItem item) {
        this.breakfastItems.remove(item);
    }

    /**
     * Removes a food item from the lunch list.
     *
     * @param item the food item to remove
     */
    public void removeFromLunch(FoodItem item) {
        this.lunchItems.remove(item);
    }

    /**
     * Removes a food item from the dinner list.
     *
     * @param item the food item to remove
     */
    public void removeFromDinner(FoodItem item) {
        this.dinnerItems.remove(item);
    }

    /**
     * Removes a food item from the snacks list.
     *
     * @param item the food item to remove
     */
    public void removeFromSnacks(FoodItem item) {
        this.snacksItems.remove(item);
    }
    
    private double computeTotalCalories() {
        return this.sumCalories(this.breakfastItems)
            + this.sumCalories(this.lunchItems)
            + this.sumCalories(this.dinnerItems)
            + this.sumCalories(this.snacksItems);
    }

    private double sumCalories(ObservableList<FoodItem> items) {
        double total = 0;
        for (FoodItem item : items) {
            total += item.getCalories();
        }
        return total;
    }
   
    //TODO: remove if unused
//    private FoodItem makeFood(String description, double calories) {
//        BaseFood food = new BaseFood();
//        food.setDescription(description);
//        food.setQuantityCategory(QuantityCategory.SERVING); 
//        food.setPortionSize(1);
//        food.setCalories(calories);
//        return food;
//    }
}
