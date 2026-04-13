package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.time.LocalDate;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private final ObservableList<FoodItem> breakfastItems;
    private final ObservableList<FoodItem> lunchItems;
    private final ObservableList<FoodItem> dinnerItems;
    private final ObservableList<FoodItem> snacksItems;
    
    private final ReadOnlyDoubleWrapper totalCalories = new ReadOnlyDoubleWrapper();
    
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final ObjectProperty<DietGoals> usersDietGoals;
    private final ObjectProperty<FoodLog> currentFoodLog;
    private final StringProperty usersName;

    /**
     * HomeDashboard VM Constructor.
     * Binds calorie totals to foods added.
     *
     * @param user the currently logged in user
     */
    public HomeDashboardViewModel(User user) {
        this.currentUser.set(user);
        this.usersName = new SimpleStringProperty(this.currentUser.get().getName());
        this.currentFoodLog = new SimpleObjectProperty<>(this.currentUser.get().getCurrentFoodLog());
        this.usersDietGoals = new SimpleObjectProperty<>(this.currentUser.get().getDietGoals());
        
        var breakfastList = this.currentUser.get().getCurrentFoodLog().getBreakfast();
        this.breakfastItems = FXCollections.observableArrayList(breakfastList);
        
        var lunchList = this.currentUser.get().getCurrentFoodLog().getLunch();
        this.lunchItems = FXCollections.observableArrayList(lunchList);
        
        var dinnerList = this.currentUser.get().getCurrentFoodLog().getDinner();
        this.dinnerItems = FXCollections.observableArrayList(dinnerList);
        
        var snackList = this.currentUser.get().getCurrentFoodLog().getSnacks();
        this.snacksItems = FXCollections.observableArrayList(snackList);
        
        DoubleBinding total = Bindings.createDoubleBinding(
            this::computeTotalCalories, 
            breakfastItems, lunchItems, dinnerItems, snacksItems
        );
        this.totalCalories.bind(total);
    }

    /**
     * Get User object.
     * 
     * @return user object.
     */
    public User getCurrentUser() {
        return this.currentUser.get();
    }
    
    /**
     * Set User object.
     * 
     * @param user to be set.
     */
    public void setCurrentUser(User user) {
        this.currentUser.set(user);
    }
    
    /**
     * Gets the users name property.
     *
     * @return the users name property
     */
    public StringProperty getUsersNameProperty() {
    	return this.usersName;
    }
    
    /**
     * User food log property.
     *
     * @return the object property
     */
    public ObjectProperty<FoodLog> userFoodLogProperty() {
    	return this.currentFoodLog;
    }
    
    /**
     * User diet goals property.
     *
     * @return the object property
     */
    public ObjectProperty<DietGoals> userDietGoalsProperty() {
    	return this.usersDietGoals;
    }
    
    /**
     * Get selected date property.
     * 
     * @return ObjectProperty<LocalDate> selected date property.
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
     * Get the user's breakfast food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getBreakfastItems() {
        return this.breakfastItems;
    }

    /**
     * Get the user's lunch food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getLunchItems() {
        return this.lunchItems;
    }

    /**
     * Get the user's dinner food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getDinnerItems() {
        return this.dinnerItems;
    }

    /**
     * Get the user's snack food items list.
     * @return ObservableList of food items.
     */
    public ObservableList<FoodItem> getSnacksItems() {
        return this.snacksItems;
    }

    /**
     * Returns a read-only property representing the user's total calories for the day.
     *
     * @return the read-only total calories property
     */
    public ReadOnlyDoubleProperty totalCaloriesProperty() {
        return this.totalCalories.getReadOnlyProperty();
    }
    
    /**
     * Adds a food item to the user's  breakfast list.
     *
     * @param item the food item to add
     */
    public void addToBreakfast(FoodItem item) {
        this.breakfastItems.add(item);
    }

    /**
     * Adds a food item to the user's  lunch list.
     *
     * @param item the food item to add
     */
    public void addToLunch(FoodItem item) {
        this.lunchItems.add(item);
    }

    /**
     * Adds a food item to the user's  dinner list.
     *
     * @param item the food item to add
     */
    public void addToDinner(FoodItem item) {
        this.dinnerItems.add(item);
    }

    /**
     * Adds a food item to the user's  snacks list.
     *
     * @param item the food item to add
     */
    public void addToSnacks(FoodItem item) {
        this.snacksItems.add(item);
    }

    /**
     * Removes a food item from the user's breakfast list.
     *
     * @param item the food item to remove
     */
    public void removeFromBreakfast(FoodItem item) {
        this.breakfastItems.remove(item);
    }

    /**
     * Removes a food item from the user's lunch list.
     *
     * @param item the food item to remove
     */
    public void removeFromLunch(FoodItem item) {
        this.lunchItems.remove(item);
    }

    /**
     * Removes a food item from the user's dinner list.
     *
     * @param item the food item to remove
     */
    public void removeFromDinner(FoodItem item) {
        this.dinnerItems.remove(item);
    }

    /**
     * Removes a food item from the user's snacks list.
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
