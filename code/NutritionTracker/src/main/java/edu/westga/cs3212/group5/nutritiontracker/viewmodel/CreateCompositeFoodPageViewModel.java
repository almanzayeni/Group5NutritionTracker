package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.util.ArrayList;
import java.util.List;

import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
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
	private StringProperty description;
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

	/**
	 * Instantiates a new creates the composite food page view model.
	 */
	public CreateCompositeFoodPageViewModel() {
		this.compositeFood = new CompositeFood();
		this.description = new SimpleStringProperty();

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
		this.ingredients = new SimpleListProperty<FoodItem>(
				FXCollections.observableArrayList(new ArrayList<FoodItem>()));
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
	 * Creates the composite food and sends it to the server.
	 *
	 * @throws IllegalArgumentException if name is null or empty, no ingredients
	 *                                  have been added, or no quantity category
	 *                                  has been selected
	 * @throws RuntimeException         if the server call fails
	 */
	public void createCompositeFood() {
		if (this.description.get() == null || this.description.get().isEmpty()) {
			throw new IllegalArgumentException("Food name cannot be empty.");
		}
		if (this.compositeFood.getIngredients().isEmpty()) {
			throw new IllegalArgumentException("At least one ingredient must be added.");
		}
		if (this.selectedQuantityCategory.get() == null) {
			throw new IllegalArgumentException("A quantity category must be selected.");
		}

		this.compositeFood.setDescription(this.description.get());
		this.compositeFood.setQuantityCategory(this.selectedQuantityCategory.get());

		String request = AddFoodRequestHandler.createAddFoodRequest(this.compositeFood);
		AddFoodRequestHandler.handleAddFoodRequest(request);

		this.clearFields();
	}

	/**
	 * Adds the ingredient passed in to the composite food
	 *
	 * @param ingredient the ingredient to add to the composite food
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
	 * Removes the ingredient passed in from the composite food
	 *
	 * @param ingredient the ingredient to remove from the composite food
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
	 * Adds the ingredients passed in to the composite food.
	 *
	 * @param ingredients the ingredients to add to the composite food
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
	 * Gets the ingredient passed in from the composite food.
	 *
	 * @param ingredientToFind the ingredient to find
	 * @throws IllegalArgumentException if ingredientToFind is null
	 * @return the ingredient if found, or null if not found
	 */
	public FoodItem getIngredient(FoodItem ingredientToFind) {
		if (ingredientToFind == null) {
			throw new IllegalArgumentException("Ingredient name cannot be null.");
		}
		return this.compositeFood.getIngredientByDescription(ingredientToFind.getDescription());
	}

	private void updateDisplayInfo() {
		this.ingredients.clear();
		this.ingredients.addAll(this.compositeFood.getIngredientsList());
		this.totalCalories.set(this.compositeFood.getCalories());
		this.totalProtein.set(this.compositeFood.getProtein());
		this.totalFat.set(this.compositeFood.getFat());
		this.totalSugar.set(this.compositeFood.getSugar());
		this.totalCarbohydrates.set(this.compositeFood.getCarbohydrates());
		this.totalSodium.set(this.compositeFood.getSodium());
	}

	private void clearFields() {
		this.description.set("");
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