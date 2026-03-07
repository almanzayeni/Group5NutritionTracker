package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CreateMealItemPageViewModel {
	private static final String MEAL_ALREADY_EXISTS_ERROR_MESSAGE = "A meal with this name already exists. Please enter a unique name, or edit the existing meal.";
	private StringProperty description;
	private double portionSize;
	private DoubleProperty totalCalories;
	private DoubleProperty totalProtein;
	private DoubleProperty totalFat;
	private DoubleProperty totalSugar;
	private DoubleProperty totalCarbohydrates;
	private DoubleProperty totalSodium;
	private ListProperty<FoodItem> foods;
	private CompositeFood mealItem;
	private String filePath;
	private ObjectMapper objectMapper;

	/**
	 * Instantiates a new creates the composite food page view model.
	 */
	public CreateMealItemPageViewModel() {
		this.mealItem = new CompositeFood();
		this.description = new SimpleStringProperty();
		this.portionSize = this.mealItem.getPortionSize();
		this.totalCalories = new SimpleDoubleProperty();
		this.totalCalories.set(this.mealItem.getCalories());
		this.totalProtein = new SimpleDoubleProperty();
		this.totalProtein.set(this.mealItem.getProtein());
		this.totalFat = new SimpleDoubleProperty();
		this.totalFat.set(this.mealItem.getFat());
		this.totalSugar = new SimpleDoubleProperty();
		this.totalSugar.set(this.mealItem.getSugar());
		this.totalCarbohydrates = new SimpleDoubleProperty();
		this.totalCarbohydrates.set(this.mealItem.getCarbohydrates());
		this.totalSodium = new SimpleDoubleProperty();
		this.totalSodium.set(this.mealItem.getSodium());
		this.foods = new SimpleListProperty<FoodItem>(
				FXCollections.observableArrayList(new ArrayList<FoodItem>()));
		this.filePath = FoodItem.FOOD_ITEMS_JSON_FILE;
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Instantiates a new creates the composite food page view model. FOR TESTING
	 * PURPOSES ONLY - allows for custom file path to be set for testing without
	 * affecting production data
	 *
	 * @param filePath the file path
	 */
	public CreateMealItemPageViewModel(String filePath) {
		this();
		this.filePath = filePath;
	}

	/**
	 * Instantiates a new creates the composite food page view model. FOR TESTING
	 * PURPOSES ONLY - allows injection of a mock ObjectMapper to simulate JSON
	 * processing exceptions.
	 *
	 * @param objectMapper the object mapper
	 */
	public CreateMealItemPageViewModel(ObjectMapper objectMapper, String filePath) {
		this();
		this.filePath = filePath;
		this.objectMapper = objectMapper;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public StringProperty getDescriptionProperty() {
		return description;
	}

	/**
	 * Gets the portion size.
	 *
	 * @return the portion size
	 */
	public double getPortionSize() {
		return portionSize;
	}

	/**
	 * Gets the total calories.
	 *
	 * @return the total calories
	 */
	public DoubleProperty getTotalCaloriesProperty() {
		return totalCalories;
	}

	/**
	 * Gets the total protein.
	 *
	 * @return the total protein
	 */
	public DoubleProperty getTotalProteinProperty() {
		return totalProtein;
	}

	/**
	 * Gets the total fat.
	 *
	 * @return the total fat
	 */
	public DoubleProperty getTotalFatProperty() {
		return totalFat;
	}

	/**
	 * Gets the total sugar.
	 *
	 * @return the total sugar
	 */
	public DoubleProperty getTotalSugarProperty() {
		return totalSugar;
	}

	/**
	 * Gets the total carbohydrates.
	 *
	 * @return the total carbohydrates
	 */
	public DoubleProperty getTotalCarbohydratesProperty() {
		return totalCarbohydrates;
	}

	/**
	 * Gets the total sodium.
	 *
	 * @return the total sodium
	 */
	public DoubleProperty getTotalSodiumProperty() {
		return totalSodium;
	}

	/**
	 * Gets the ingredients.
	 *
	 * @return the ingredients
	 */
	public ListProperty<FoodItem> getIngredientsListProperty() {
		return foods;
	}

	/**
	 * Creates the meal item.
	 *
	 * @throws IllegalArgumentException if any of the following conditions occur:
	 *                                  name is null or empty, no foods have
	 *                                  been added, or a meal with the same name
	 *                                  already exists
	 * @throws JsonProcessingException  thrown if there is an error during JSON
	 *                                  processing when converting the composite
	 *                                  food to a JSON string
	 * @throws IOException              Signals that an I/O exception has occurred.
	 */
	public void createMealItem() throws IllegalArgumentException, JsonProcessingException, IOException {
		if (this.description.get() == null || this.description.get().isEmpty()) {
			throw new IllegalArgumentException("Meal name cannot be empty.");
		}
		if (this.mealItem.getIngredients().isEmpty()) {
			throw new IllegalArgumentException("At least one food must be added.");
		}
		if (this.checkForExistingMeal(this.description.get())) {
			throw new IllegalArgumentException(MEAL_ALREADY_EXISTS_ERROR_MESSAGE);
		}

		String jsonString = "";
		this.mealItem.setDescription(this.description.get());
		this.mealItem.setQuantityCategory(QuantityCategory.SERVING);

		try {
			jsonString = this.objectMapper.writeValueAsString(this.mealItem);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}

		try {
			// TODO: Send jsonString to server
			Files.write(Paths.get(this.filePath), (jsonString + System.lineSeparator()).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		this.clearFields();
	}

	/**
	 * Adds the food passed in to the meal
	 *
	 * @param food the food to add to the meal
	 * @throws IllegalArgumentException if food is null or already exists in the meal
	 */
	public void addFood(FoodItem food) {
		if (food == null) {
			throw new IllegalArgumentException("No food selected.");
		}
		if (this.mealItem.getIngredientByDescription(food.getDescription()) != null) {
			throw new IllegalArgumentException(
					"Food already exists in the meal. Please update the portion size of the existing food.");
		}
		this.mealItem.addIngredient(food);
		this.updateDisplayInfo();
	}

	/**
	 * Removes the food passed in from the meal
	 *
	 * @param food the ingredient to remove from the meal
	 * @throws IllegalArgumentException if food is null or does not exist in the meal
	 */
	public void removeFood(FoodItem food) {
		if (food == null) {
			throw new IllegalArgumentException("Ingredient cannot be null.");
		}
		if (!this.mealItem.removeIngredientByDescription(food.getDescription())) {
			throw new IllegalArgumentException("Ingredient does not exist in the food.");
		}
		this.updateDisplayInfo();
	}

	/**
	 * Adds the foods passed in to the meal.
	 *
	 * @param mealFoods the foods to add to the meal
	 * @throws IllegalArgumentException if mealFoods is null or empty, or if any food in mealFoods already exists in the meal
	 */
	public void addFoods(List<FoodItem> mealFoods) {
		if (mealFoods == null || mealFoods.isEmpty()) {
			throw new IllegalArgumentException("MealFoods list cannot be null or empty.");
		}
		for (FoodItem food : mealFoods) {
			this.addFood(food);
		}
	}

	/**
	 * Gets the food passed in from the meal.
	 *
	 * @param foodToFind the food to find
	 * @throws IllegalArgumentException if foodToFind is null
	 * @return the food if found, or null if not found
	 */
	public FoodItem getFood(FoodItem foodToFind) {
		if (foodToFind == null) {
			throw new IllegalArgumentException("Food to find cannot be null.");
		}

		return this.mealItem.getIngredientByDescription(foodToFind.getDescription());
	}

	private boolean checkForExistingMeal(String mealName) throws IOException {
		HashSet<String> existingMealNames = new HashSet<>();
		try (var lines = Files.lines(Paths.get(this.filePath))) {
			lines.forEach(line -> {
				try {
					CompositeFood food = this.objectMapper.readValue(line, CompositeFood.class);
					existingMealNames.add(food.getDescription());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return existingMealNames.contains(mealName);
	}

	private void updateDisplayInfo() {
		this.foods.clear();
		this.foods.addAll(this.mealItem.getIngredientsList());
		this.totalCalories.set(this.mealItem.getCalories());
		this.totalProtein.set(this.mealItem.getProtein());
		this.totalFat.set(this.mealItem.getFat());
		this.totalSugar.set(this.mealItem.getSugar());
		this.totalCarbohydrates.set(this.mealItem.getCarbohydrates());
		this.totalSodium.set(this.mealItem.getSodium());
	}

	private void clearFields() {
		this.description.set("");
		this.totalCalories.set(0);
		this.totalProtein.set(0);
		this.totalFat.set(0);
		this.totalSugar.set(0);
		this.totalCarbohydrates.set(0);
		this.totalSodium.set(0);
		this.foods.clear();
	}
}
