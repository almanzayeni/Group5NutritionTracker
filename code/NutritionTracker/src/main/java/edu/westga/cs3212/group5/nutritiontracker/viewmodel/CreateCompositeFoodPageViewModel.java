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

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CreateCompositeFoodPageViewModel {
	private static final String FOOD_ALREADY_EXISTS_ERROR_MESSAGE = "A food with this name already exists. Please enter a unique name, or edit the existing food item.";
	private static final String COMPOSITE_FOOD_ITEMS_JSON_FILE = "composite_food_items.json";
	private StringProperty name;
	private ListProperty<QuantityCategory> quantityCategories;
	private ObjectProperty<QuantityCategory> selectedQuantityCategory;
	private double portionSize;
	private DoubleProperty totalCalories;
	private DoubleProperty totalProtein;
	private DoubleProperty totalFat;
	private DoubleProperty totalSugar;
	private DoubleProperty totalCarbohydrates;
	private DoubleProperty totalSodium;
	private ListProperty<FoodItem> ingredients;
	private CompositeFood compositeFood;
	private String filePath;
	private ObjectMapper objectMapper;

	/**
	 * Instantiates a new creates the composite food page view model.
	 */
	public CreateCompositeFoodPageViewModel() {
		this.compositeFood = new CompositeFood();
		this.name = new SimpleStringProperty();

		ArrayList<QuantityCategory> quantityCategories = new ArrayList<>();
		quantityCategories.add(QuantityCategory.QUANTITY);
		quantityCategories.add(QuantityCategory.WEIGHT);
		quantityCategories.add(QuantityCategory.SERVING);

		this.quantityCategories = new SimpleListProperty<QuantityCategory>(
				FXCollections.observableArrayList(quantityCategories));
		this.selectedQuantityCategory = new SimpleObjectProperty<QuantityCategory>();
		this.portionSize = this.compositeFood.getPortionSize();
		this.totalCalories = new SimpleDoubleProperty();
		this.totalCalories.set(this.compositeFood.getCalories());
		this.totalProtein = new SimpleDoubleProperty();
		this.totalProtein.set(this.compositeFood.getProtein());
		this.totalFat = new SimpleDoubleProperty();
		this.totalFat.set(this.compositeFood.getFat());
		this.totalSugar = new SimpleDoubleProperty();
		this.totalSugar.set(this.compositeFood.getSugar());
		this.totalCarbohydrates = new SimpleDoubleProperty();
		this.totalCarbohydrates.set(this.compositeFood.getCarbohydrates());
		this.totalSodium = new SimpleDoubleProperty();
		this.totalSodium.set(this.compositeFood.getSodium());
		this.ingredients = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<FoodItem>()));
		this.filePath = COMPOSITE_FOOD_ITEMS_JSON_FILE;
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Instantiates a new creates the composite food page view model. FOR TESTING
	 * PURPOSES ONLY - allows for custom file path to be set for testing without
	 * affecting production data
	 *
	 * @param filePath the file path
	 */
	public CreateCompositeFoodPageViewModel(String filePath) {
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
	public CreateCompositeFoodPageViewModel(ObjectMapper objectMapper) {
		this();
		this.objectMapper = objectMapper;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public StringProperty getNameProperty() {
		return name;
	}

	/**
	 * Gets the quantity categories.
	 *
	 * @return the quantity categories
	 */
	public ListProperty<QuantityCategory> getQuantityCategoriesListPropery() {
		return quantityCategories;
	}

	/**
	 * Gets the selected quantity category.
	 *
	 * @return the selected quantity category
	 */
	public ObjectProperty<QuantityCategory> getSelectedQuantityCategoryProperty() {
		return selectedQuantityCategory;
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
		return ingredients;
	}

	/**
	 * Creates the composite food.
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws JsonProcessingException  the json processing exception
	 * @throws IOException              Signals that an I/O exception has occurred.
	 */
	public void createCompositeFood() throws IllegalArgumentException, JsonProcessingException, IOException {
		if (this.name.get() == null || this.name.get().isEmpty()) {
			throw new IllegalArgumentException("Food name cannot be empty.");
		}
		if (this.ingredients.get() == null || this.ingredients.get().isEmpty()) {
			throw new IllegalArgumentException("At least one ingredient must be added.");
		}
		if (this.checkForExistingFood(this.name.get())) {
			throw new IllegalArgumentException(FOOD_ALREADY_EXISTS_ERROR_MESSAGE);
		}

		String jsonString = "";
		this.compositeFood.setDescription(this.name.get());
		this.compositeFood.setQuantityCategory(this.selectedQuantityCategory.get());

		try {
			jsonString = this.objectMapper.writeValueAsString(this.compositeFood);
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
	 * Adds the ingredient passed in to the selected ingredients.
	 *
	 * @param ingredient the ingredient to add to the selected ingredients
	 */
	public void addIngredient(FoodItem ingredient) {
		if (ingredient == null) {
			throw new IllegalArgumentException("No ingredient selected.");
		}
		if (this.compositeFood.getIngredientByDescription(ingredient.getDescription()) != null) {
			throw new IllegalArgumentException(
					"Ingredient already exists in the food item. Please update the portion size of the existing ingredient.");
		}
		this.compositeFood.addIngredient(ingredient);
		this.updateDisplayInfo();
	}

	/**
	 * Removes the ingredient passed in from the selected ingredients.
	 *
	 * @param ingredient the ingredient to remove from the selected ingredients
	 */
	public void removeIngredient(FoodItem ingredient) {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient cannot be null.");
		}
		if (!this.compositeFood.removeIngredientByDescription(ingredient.getDescription())) {
			throw new IllegalArgumentException("Ingredient does not exist in the food.");
		}
		this.updateDisplayInfo();
	}

	/**
	 * Adds the ingredients passed in to the selected ingredients.
	 *
	 * @param ingredients the ingredients to add to the selected ingredients
	 */
	public void addIngredients(List<FoodItem> ingredients) {
		if (ingredients == null || ingredients.isEmpty()) {
			throw new IllegalArgumentException("Ingredients list cannot be null or empty.");
		}
		for (FoodItem ingredient : ingredients) {
			this.addIngredient(ingredient);
		}
	}

	/**
	 * Gets the ingredient passed in from the selected ingredients.
	 *
	 * @param ingredientToFind the ingredient to find
	 * @throws IllegalArgumentException if ingredientToFind is null or not found in the composite food
	 * @return the ingredient
	 */
	public FoodItem getIngredient(FoodItem ingredientToFind) {
		if (ingredientToFind == null) {
			throw new IllegalArgumentException("Ingredient name cannot be null.");
		}
		
		return this.compositeFood.getIngredientByDescription(ingredientToFind.getDescription());
	}

	private boolean checkForExistingFood(String foodName) {
		HashSet<String> existingFoodNames = new HashSet<>();
		try {
			Files.lines(Paths.get(this.filePath)).forEach(line -> {
				try {
					BaseFood food = new ObjectMapper().readValue(line, BaseFood.class);
					existingFoodNames.add(food.getDescription());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return existingFoodNames.contains(foodName);
	}

	private void updateDisplayInfo() {
//		double totalCalories = 0;
//		double totalProtein = 0;
//		double totalFat = 0;
//		double totalSugar = 0;
//		double totalCarbohydrates = 0;
//		double totalSodium = 0;
//
//		for (FoodItem ingredient : ingredients.get()) {
//			totalCalories += ingredient.getCalories() * ingredient.getPortionSize();
//			totalProtein += ingredient.getProtein() * ingredient.getPortionSize();
//			totalFat += ingredient.getFat() * ingredient.getPortionSize();
//			totalSugar += ingredient.getSugar() * ingredient.getPortionSize();
//			totalCarbohydrates += ingredient.getCarbohydrates() * ingredient.getPortionSize();
//			totalSodium += ingredient.getSodium() * ingredient.getPortionSize();
//		}
		this.ingredients.clear();
		this.ingredients.addAll(this.compositeFood.getIngredients());
		this.totalCalories.set(this.compositeFood.getCalories());
		this.totalProtein.set(this.compositeFood.getProtein());
		this.totalFat.set(this.compositeFood.getFat());
		this.totalSugar.set(this.compositeFood.getSugar());
		this.totalCarbohydrates.set(this.compositeFood.getCarbohydrates());
		this.totalSodium.set(this.compositeFood.getSodium());
	}

	private void clearFields() {
		this.name.set("");
		this.selectedQuantityCategory.set(null);
		this.totalCalories.set(0);
		this.totalProtein.set(0);
		this.totalFat.set(0);
		this.totalSugar.set(0);
		this.totalCarbohydrates.set(0);
		this.totalSodium.set(0);
		this.ingredients.clear();
	}
}
