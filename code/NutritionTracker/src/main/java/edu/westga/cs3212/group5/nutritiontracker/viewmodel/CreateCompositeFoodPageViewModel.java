package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;

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
	private DoubleProperty calories;
	private DoubleProperty protein;
	private DoubleProperty fat;
	private DoubleProperty sugar;
	private DoubleProperty carbohydrates;
	private DoubleProperty sodium;
	private ListProperty<FoodItem> ingredients;

	public CreateCompositeFoodPageViewModel() {
		this.name = new SimpleStringProperty();

		ArrayList<QuantityCategory> quantityCategories = new ArrayList<>();
		quantityCategories.add(QuantityCategory.QUANTITY);
		quantityCategories.add(QuantityCategory.WEIGHT);
		quantityCategories.add(QuantityCategory.SERVING);

		this.quantityCategories = new SimpleListProperty<QuantityCategory>(
				FXCollections.observableArrayList(quantityCategories));
		this.selectedQuantityCategory = new SimpleObjectProperty<QuantityCategory>();
		this.portionSize = 1;
		this.calories = new SimpleDoubleProperty();
		this.calories.set(0);
		this.protein = new SimpleDoubleProperty();
		this.protein.set(0);
		this.fat = new SimpleDoubleProperty();
		this.fat.set(0);
		this.sugar = new SimpleDoubleProperty();
		this.sugar.set(0);
		this.carbohydrates = new SimpleDoubleProperty();
		this.carbohydrates.set(0);
		this.sodium = new SimpleDoubleProperty();
		this.sodium.set(0);
		this.ingredients = new SimpleListProperty<>();
	}

	public StringProperty getName() {
		return name;
	}

	public ListProperty<QuantityCategory> getQuantityCategories() {
		return quantityCategories;
	}

	public ObjectProperty<QuantityCategory> getSelectedQuantityCategory() {
		return selectedQuantityCategory;
	}

	public DoubleProperty getCalories() {
		return calories;
	}

	public DoubleProperty getProtein() {
		return protein;
	}

	public DoubleProperty getFat() {
		return fat;
	}

	public DoubleProperty getSugar() {
		return sugar;
	}

	public DoubleProperty getCarbohydrates() {
		return carbohydrates;
	}

	public DoubleProperty getSodium() {
		return sodium;
	}

	public ListProperty<FoodItem> getIngredients() {
		return ingredients;
	}

	public void updateNutritionInfo() {
		double totalCalories = 0;
		double totalProtein = 0;
		double totalFat = 0;
		double totalSugar = 0;
		double totalCarbohydrates = 0;
		double totalSodium = 0;

		for (FoodItem ingredient : ingredients.get()) {
			totalCalories += ingredient.getCalories() * ingredient.getPortionSize();
			totalProtein += ingredient.getProtein() * ingredient.getPortionSize();
			totalFat += ingredient.getFat() * ingredient.getPortionSize();
			totalSugar += ingredient.getSugar() * ingredient.getPortionSize();
			totalCarbohydrates += ingredient.getCarbohydrates() * ingredient.getPortionSize();
			totalSodium += ingredient.getSodium() * ingredient.getPortionSize();
		}

		this.calories.set(totalCalories);
		this.protein.set(totalProtein);
		this.fat.set(totalFat);
		this.sugar.set(totalSugar);
		this.carbohydrates.set(totalCarbohydrates);
		this.sodium.set(totalSodium);
	}

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

		this.updateNutritionInfo();
		CompositeFood compositeFood = new CompositeFood(this.name.get(), this.selectedQuantityCategory.get(),
				this.portionSize, this.ingredients.get(), this.calories.get(), this.protein.get(), this.fat.get(),
				this.sugar.get(), this.carbohydrates.get(), this.sodium.get());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";

		try {
			jsonString = objectMapper.writeValueAsString(compositeFood);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}

		try {
			// TODO: Send jsonString to server
			Files.write(Paths.get(COMPOSITE_FOOD_ITEMS_JSON_FILE), (jsonString + System.lineSeparator()).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		this.clearFields();
	}

	private boolean checkForExistingFood(String foodName) {
		HashSet<String> existingFoodNames = new HashSet<>();
		try {
			Files.lines(Paths.get(COMPOSITE_FOOD_ITEMS_JSON_FILE)).forEach(line -> {
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

	private void clearFields() {
		this.name.set("");
		this.selectedQuantityCategory.set(null);
		this.calories.set(0);
		this.protein.set(0);
		this.fat.set(0);
		this.sugar.set(0);
		this.carbohydrates.set(0);
		this.sodium.set(0);
		this.ingredients.clear();
	}
	
	public void addIngredient(FoodItem ingredient) {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient cannot be null.");
		}
		if (this.checkForExistingFood(ingredient.getDescription())) {
			throw new IllegalArgumentException("Ingredient already exists in the food item. Please update the portion size of the existing ingredient.");
		}
		this.ingredients.add(ingredient);
		this.updateNutritionInfo();
	}
	
	public void removeIngredient(FoodItem ingredient) {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient cannot be null.");
		}
		if (!this.ingredients.contains(ingredient)) {
			throw new IllegalArgumentException("Ingredient does not exist in the food.");
		}
		this.ingredients.remove(ingredient);
		this.updateNutritionInfo();
	}
	
	public void addIngredients(ArrayList<FoodItem> ingredients) {
		if (ingredients == null || ingredients.isEmpty()) {
			throw new IllegalArgumentException("Ingredients list cannot be null or empty.");
		}
		for (FoodItem ingredient : ingredients) {
			this.addIngredient(ingredient);
		}
	}
	
	public FoodItem getIngredient(FoodItem ingredientToFind) {
		if (ingredientToFind == null) {
			throw new IllegalArgumentException("Ingredient name cannot be null.");
		}
		for (FoodItem ingredient : this.ingredients.get()) {
			if (ingredient.getDescription().equals(ingredientToFind.getDescription())) {
				return ingredient;
			}
		}
		throw new IllegalArgumentException("Ingredient not found.");
	}
}
