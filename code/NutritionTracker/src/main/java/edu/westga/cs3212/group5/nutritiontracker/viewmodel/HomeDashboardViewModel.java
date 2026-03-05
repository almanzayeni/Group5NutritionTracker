package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.time.LocalDate;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
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
 */
public class HomeDashboardViewModel {
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());
    private final ObservableList<FoodItem> breakfastItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> lunchItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> dinnerItems = FXCollections.observableArrayList();
    private final ObservableList<FoodItem> snacksItems = FXCollections.observableArrayList();
    
    private final ReadOnlyDoubleWrapper totalCalories = new ReadOnlyDoubleWrapper();
    
    public HomeDashboardViewModel() {
    	this.setUpSampleData();
    	
    	DoubleBinding total = Bindings.createDoubleBinding(this::computeTotalCalories, breakfastItems, lunchItems, dinnerItems, snacksItems);
    	this.totalCalories.bind(total);
    }

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public void setSelectedDate(LocalDate date) {
        selectedDate.set(date);
    }
    
    public ObservableList<FoodItem> getBreakfastItems() {
        return this.breakfastItems;
    }

    public ObservableList<FoodItem> getLunchItems() {
        return this.lunchItems;
    }

    public ObservableList<FoodItem> getDinnerItems() {
        return this.dinnerItems;
    }

    public ObservableList<FoodItem> getSnacksItems() {
        return this.snacksItems;
    }

    public ReadOnlyDoubleProperty totalCaloriesProperty() {
        return this.totalCalories.getReadOnlyProperty();
    }
    
    public void addToBreakfast(FoodItem item) {
        this.breakfastItems.add(item);
    }

    public void addToLunch(FoodItem item) {
        this.lunchItems.add(item);
    }

    public void addToDinner(FoodItem item) {
        this.dinnerItems.add(item);
    }

    public void addToSnacks(FoodItem item) {
        this.snacksItems.add(item);
    }

    public void removeFromBreakfast(FoodItem item) {
        this.breakfastItems.remove(item);
    }

    public void removeFromLunch(FoodItem item) {
        this.lunchItems.remove(item);
    }

    public void removeFromDinner(FoodItem item) {
        this.dinnerItems.remove(item);
    }

    public void removeFromSnacks(FoodItem item) {
        this.snacksItems.remove(item);
    }
    
    private double computeTotalCalories() {
        return sum(breakfastItems)
            + sum(lunchItems)
            + sum(dinnerItems)
            + sum(snacksItems);
    }

    private double sum(ObservableList<FoodItem> items) {
        double total = 0;
        for (FoodItem item : items) {
            total += item.getCalories();
        }
        return total;
    }
    
    private void setUpSampleData() {
        breakfastItems.add(makeFood("Eggs", 140));
        breakfastItems.add(makeFood("Toast", 120));

        lunchItems.add(makeFood("Turkey Hoagie", 520));
        lunchItems.add(makeFood("Grapes", 90));

        dinnerItems.add(makeFood("Angel Hair Pasta", 380));
        dinnerItems.add(makeFood("Air Fried Chicken", 220));

        snacksItems.add(makeFood("Chocolate Milk", 180));
    }

    private FoodItem makeFood(String description, double calories) {
        BaseFood food = new BaseFood();
        food.setDescription(description);
        food.setQuantityCategory(QuantityCategory.SERVING); 
        food.setPortionSize(1);
        food.setCalories(calories);
        return food;
    }
}
