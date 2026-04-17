package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.westga.cs3212.group5.nutritiontracker.model.DashboardCalculations;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.MealType;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
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
 * @author vfilpo + Emi :), Yeni Almanza
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
	private final ObjectProperty<PrimaryGoal> selectedGoal = new SimpleObjectProperty<>(PrimaryGoal.CALORIE);
	private final ObjectProperty<DashboardCalculations> calculations = new SimpleObjectProperty<>();
	private final ObjectProperty<DietGoals> usersDietGoals;
	private final ObjectProperty<FoodLog> currentFoodLog;
	private final StringProperty usersName;

  private MealType pendingMealType = null;
  
	/**
	 * HomeDashboard VM Constructor. Binds calorie totals to foods added.
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

		DoubleBinding total = Bindings.createDoubleBinding(this::computeTotalCalories, breakfastItems, lunchItems,
				dinnerItems, snacksItems);
		this.totalCalories.bind(total);
		
		this.calculations.bind(Bindings.createObjectBinding(
			    () -> create(this.currentUser.get(), this.selectedGoal.get()),
			    this.currentUser,
			    this.selectedGoal,
			    this.breakfastItems,
			    this.lunchItems,
			    this.dinnerItems,
			    this.snacksItems
			));
	}
	
	/**
	 * Gets selected goal property.
	 * @return currently selected primary goal
	 */
	public ObjectProperty<PrimaryGoal> selectedGoalProperty() {
	    return this.selectedGoal;
	}
  
  /**
   * Sets the meal type that should receive the next food the user picks.
   *
   * @precondition mealType != null
   * @param mealType the target meal
   * @throws IllegalArgumentException if mealType is null
   */
  public void setPendingMealType(MealType mealType) {
      if (mealType == null) {
          throw new IllegalArgumentException("Meal type cannot be null");
      }
      this.pendingMealType = mealType;
  }

  /**
   * Returns the meal type the user is currently adding food to, or null if none
   * is set.
   *
   * @return the pending meal type
   */
  public MealType getPendingMealType() {
      return this.pendingMealType;
  }

  /**
   * Clears the pending meal type after the food has been added.
   */
  public void clearPendingMealType() {
      this.pendingMealType = null;
  }

  /**
   * Adds a food item to the meal list indicated by {@link #getPendingMealType()}.
   *
   * @precondition food != null && pendingMealType != null
   * @param food the food item to add
   * @throws IllegalArgumentException if food is null
   * @throws IllegalStateException    if no pending meal type has been set
   */
  public void addFoodToPendingMeal(FoodItem food) {
      if (food == null) {
          throw new IllegalArgumentException("Food item cannot be null");
      }
      if (this.pendingMealType == null) {
          throw new IllegalStateException("No pending meal type set");
      }
      switch (this.pendingMealType) {
          case BREAKFAST -> this.breakfastItems.add(food);
          case LUNCH     -> this.lunchItems.add(food);
          case DINNER    -> this.dinnerItems.add(food);
          case SNACKS    -> this.snacksItems.add(food);
      }
      this.pendingMealType = null;
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
	 * 
	 * @return ObservableList of food items.
	 */
	public ObservableList<FoodItem> getBreakfastItems() {
		return this.breakfastItems;
	}

	/**
	 * Get the user's lunch food items list.
	 * 
	 * @return ObservableList of food items.
	 */
	public ObservableList<FoodItem> getLunchItems() {
		return this.lunchItems;
	}

	/**
	 * Get the user's dinner food items list.
	 * 
	 * @return ObservableList of food items.
	 */
	public ObservableList<FoodItem> getDinnerItems() {
		return this.dinnerItems;
	}

	/**
	 * Get the user's snack food items list.
	 * 
	 * @return ObservableList of food items.
	 */
	public ObservableList<FoodItem> getSnacksItems() {
		return this.snacksItems;
	}

	/**
	 * Returns a read-only property representing the user's total calories for the
	 * day.
	 *
	 * @return the read-only total calories property
	 */
	public ReadOnlyDoubleProperty totalCaloriesProperty() {
		return this.totalCalories.getReadOnlyProperty();
	}

	/**
	 * Adds a food item to the user's breakfast list.
	 *
	 * @param item the food item to add
	 */
	public void addToBreakfast(FoodItem item) {
		this.breakfastItems.add(item);
	}

	/**
	 * Adds a food item to the user's lunch list.
	 *
	 * @param item the food item to add
	 */
	public void addToLunch(FoodItem item) {
		this.lunchItems.add(item);
	}

	/**
	 * Adds a food item to the user's dinner list.
	 *
	 * @param item the food item to add
	 */
	public void addToDinner(FoodItem item) {
		this.dinnerItems.add(item);
	}

	/**
	 * Adds a food item to the user's snacks list.
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

	public static DashboardCalculations create(User user, PrimaryGoal selectedGoal) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null!");
		}
		if (selectedGoal == null) {
			throw new IllegalArgumentException("Selected goal cannot be null!");
		}

		FoodLog log = user.getCurrentFoodLog();
		double consumedAmount = calculateConsumedAmount(log, selectedGoal);
		double targetAmount = getTargetAmount(user.getDietGoals(), selectedGoal);

    return new DashboardCalculations(selectedGoal, consumedAmount, targetAmount);
}

private static double calculateConsumedAmount(FoodLog log, PrimaryGoal selectedGoal) {
	double total = 0.0;

  for (FoodItem item : getAllFoods(log)) {
		switch (selectedGoal) {
		case CALORIE:
			total += item.getCalories();
			break;
		case PROTEIN:
			total += item.getProtein();
			break;
		case FAT:
			total += item.getFat();
			break;
		case SUGAR:
			total += item.getSugar();
			break;
		case SODIUM:
			total += item.getSodium();
			break;
		case CARBS:
			total += item.getCarbohydrates();
			break;
		case OTHER:
			break;
		}
	}
	return total;
}
	
private static double getTargetAmount(DietGoals goals, PrimaryGoal selectedGoal) {
	switch (selectedGoal) {
    case CALORIE:
        return goals.getCalorieGoal();
    case PROTEIN:
        return goals.getProteinGoal();
    case FAT:
        return goals.getFatGoal();
    case SUGAR:
        return goals.getSugarGoal();
    case SODIUM:
        return goals.getSodiumGoal();
    case CARBS:
	       return goals.getCarbsGoal();
	   case OTHER:
	       return 0;
	   default:
	       return 0;
	   }
}

private double computeTotalCalories() {
	return this.sumCalories(this.breakfastItems) + this.sumCalories(this.lunchItems)
			+ this.sumCalories(this.dinnerItems) + this.sumCalories(this.snacksItems);
}

private double sumCalories(ObservableList<FoodItem> items) {
	double total = 0;
	for (FoodItem item : items) {
		total += item.getCalories();
	}
	return total;
}
	
 private static List<FoodItem> getAllFoods(FoodLog log) {
	   List<FoodItem> all = new ArrayList<>();
	   all.addAll(log.getBreakfast());
	   all.addAll(log.getLunch());
	   all.addAll(log.getDinner());
	   all.addAll(log.getSnacks());
	   return all;
}

// TODO: remove if unused
//    private FoodItem makeFood(String description, double calories) {
//        BaseFood food = new BaseFood();
//        food.setDescription(description);
//        food.setQuantityCategory(QuantityCategory.SERVING); 
//        food.setPortionSize(1);
//        food.setCalories(calories);
//        return food;
//    }
}
