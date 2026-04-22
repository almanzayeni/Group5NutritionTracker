package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.westga.cs3212.group5.nutritiontracker.model.DashboardCalculations;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.MealType;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.GetDayOfFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.UpdateFoodLogRequestHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Dashboard VM
 *
 * @author vfilpo + Emi :), Yeni Almanza, Justin Smith
 * @version Spring 2026
 */
public class HomeDashboardViewModel {
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M-d-yyyy");
	private ObjectProperty<LocalDate> selectedDate;
	private StringProperty dateString;
	private ListProperty<FoodItem> breakfastItems;
	private ListProperty<FoodItem> lunchItems;
	private ListProperty<FoodItem> dinnerItems;
	private ListProperty<FoodItem> snacksItems;
	private User currentUser;
	private ObjectProperty<PrimaryGoal> selectedGoal;
	private ObjectProperty<DashboardCalculations> calculations;
	private ObjectProperty<DietGoals> usersDietGoals;
	private StringProperty usersName;
	
	private MealType pendingMealType = null;
	private final ReadOnlyDoubleWrapper totalCalories;

	/**
	 * HomeDashboard VM Constructor. Binds calorie totals to foods added. Sets User
	 * for session.
	 *
	 * @param user the currently logged in user
	 */
	public HomeDashboardViewModel(User user) {
		this.currentUser = user;
		this.selectedDate = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		this.dateString = new SimpleStringProperty();
		this.dateString.bind(Bindings.createStringBinding(() -> DATE_FMT.format(this.selectedDate.get()), this.selectedDate));
		this.breakfastItems = new SimpleListProperty<FoodItem>(FXCollections.observableArrayList(this.currentUser.getCurrentFoodLog().getBreakfast()));
		this.lunchItems = new SimpleListProperty<FoodItem>(FXCollections.observableArrayList(this.currentUser.getCurrentFoodLog().getLunch()));
		this.dinnerItems = new SimpleListProperty<FoodItem>(FXCollections.observableArrayList(this.currentUser.getCurrentFoodLog().getDinner()));
		this.snacksItems = new SimpleListProperty<FoodItem>(FXCollections.observableArrayList(this.currentUser.getCurrentFoodLog().getSnacks()));
		this.selectedGoal = new SimpleObjectProperty<PrimaryGoal>(PrimaryGoal.CALORIE);
		this.usersName = new SimpleStringProperty(this.currentUser.getName());
		this.usersDietGoals = new SimpleObjectProperty<DietGoals>(this.currentUser.getDietGoals());
		
		this.totalCalories = new ReadOnlyDoubleWrapper();
		DoubleBinding total = Bindings.createDoubleBinding(this::computeTotalCalories, breakfastItems, lunchItems,
				dinnerItems, snacksItems);
		this.totalCalories.bind(total);
		
		this.calculations = new SimpleObjectProperty<DashboardCalculations>();
		this.calculations.bind(Bindings.createObjectBinding(
				() -> create(this.currentUser, this.selectedGoal.get()), this.selectedGoal,
				this.breakfastItems, this.lunchItems, this.dinnerItems, this.snacksItems));
	}
	
	/**
	 * Get User object.
	 * 
	 * @return user object.
	 */
	public User getCurrentUser() {
		return this.currentUser;
	}

	/**
	 * Set User object.
	 * 
	 * @param user to be set.
	 */
	public void setCurrentUser(User user) {
		this.currentUser = user;
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
	 * Get selected date string property.
	 * 
	 * @return StringProperty of selected date formatted as M-d-yyyy
	 */
	public StringProperty getDateStringProperty() {
		return this.dateString;
	}
	
	/**
	 * Gets selected goal property.
	 * 
	 * @return currently selected primary goal
	 */
	public ObjectProperty<PrimaryGoal> selectedGoalProperty() {
		return this.selectedGoal;
	}

	/**
	 * Get the user's breakfast food items list.
	 * 
	 * @return breakfast list property containing food items.
	 */
	public ListProperty<FoodItem> getBreakfastItems() {
		return this.breakfastItems;
	}

	/**
	 * Get the user's lunch food items list.
	 * 
	 * @return lunch list property containing food items.
	 */
	public ListProperty<FoodItem> getLunchItems() {
		return this.lunchItems;
	}

	/**
	 * Get the user's dinner food items list.
	 * 
	 * @return dinner list property containing food items.
	 */
	public ListProperty<FoodItem> getDinnerItems() {
		return this.dinnerItems;
	}

	/**
	 * Get the user's snack food items list.
	 * 
	 * @return snacks list property containing food items.
	 */
	public ListProperty<FoodItem> getSnacksItems() {
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
	 * Removes a food item from the specified meal type list.
	 *
	 * @param item     the food item to remove
	 * @param mealType the meal type to remove from
	 */
	public void removeFoodFromMeal(FoodItem item, MealType mealType) {
		this.currentUser.getCurrentFoodLog().removeFoodFromMeal(mealType, item);
		
		String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(this.getCurrentUser());
		UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(request);
		this.updateMealLists();
	}
	
	/**
	 * Fetches the food log for the newly selected date from the server and updates
	 * 
	 * @throws RuntimeException if the server request fails or returns an invalid response
	 */
	public void handleDateChange() {
		try {
			FoodLog log = GetDayOfFoodRequestHandler.handleRequest(this.currentUser.getUsername(), this.selectedDate.get());
			this.currentUser.setCurrentFoodLog(log);
			this.updateMealLists();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
		this.currentUser.getCurrentFoodLog().addFoodToMeal(this.pendingMealType, food);
		
		this.pendingMealType = null;

		String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(this.getCurrentUser());
		UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(request);
		this.updateMealLists();
	}

	

	/**
	 * Creates a {@link DashboardCalculations} instance for the given user and goal.
	 * <p>
	 * Derives consumed and target amounts from the user's current food log and diet
	 * goals, scoped to the specified {@link PrimaryGoal}.
	 *
	 * @param user         the user whose food log and diet goals are used; must not
	 *                     be null
	 * @param selectedGoal the goal to calculate consumption and targets for; must
	 *                     not be null
	 * @return a new {@link DashboardCalculations} populated with the derived values
	 * @throws IllegalArgumentException if {@code user} or {@code selectedGoal} is
	 *                                  null
	 */
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

	private void updateMealLists() {
		this.breakfastItems.setAll(this.currentUser.getCurrentFoodLog().getBreakfast());
		this.lunchItems.setAll(this.currentUser.getCurrentFoodLog().getLunch());
		this.dinnerItems.setAll(this.currentUser.getCurrentFoodLog().getDinner());
		this.snacksItems.setAll(this.currentUser.getCurrentFoodLog().getSnacks());
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
		if (selectedGoal == PrimaryGoal.CALORIE) {
			return goals.getCalorieGoal();
		} else if (selectedGoal == PrimaryGoal.PROTEIN) {
			return goals.getProteinGoal();
		} else if (selectedGoal == PrimaryGoal.FAT) {
			return goals.getFatGoal();
		} else if (selectedGoal == PrimaryGoal.SUGAR) {
			return goals.getSugarGoal();
		} else if (selectedGoal == PrimaryGoal.SODIUM) {
			return goals.getSodiumGoal();
		} else if (selectedGoal == PrimaryGoal.CARBS) {
			return goals.getCarbsGoal();
		} else {
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
}
