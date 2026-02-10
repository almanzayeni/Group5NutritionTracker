package edu.westga.cs3212.group5.nutritiontracker.model;

import java.util.List;
import java.util.Map;

/**
 * The Class CompositeFood.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class CompositeFood implements FoodItem {
	private String Description;
	private QuantityCategory quantityCategory;
	private double quantityValue;
	private double calories;
	private double protein = 0.0;
	private double fat = 0.0;
	private double sugar = 0.0;
	private double carbohydrates = 0.0;
	private double sodium = 0.0;
	private Map<String, FoodItem> ingredients;
	
	
	/**
	 * Instantiates a new composite food.
	 *
	 * @precondition description != null && !description.isBlank() && quantityCategory != null && quantityValue >= 0 && ingredients != null && !ingredients.isEmpty() && no duplicate descriptions in ingredients
	 *
	 * @param description the description
	 * @param quantityCategory the quantity category
	 * @param quantityValue the quantity value
	 * @param ingredients the ingredients
	 * @throws IllegalArgumentException if description is null or blank, quantity category is null, quantity value is negative, or ingredients is null or empty or contains duplicate descriptions
	 */
	public CompositeFood(String description, QuantityCategory quantityCategory, double quantityValue, List<FoodItem> ingredients) {
		if (ingredients == null || ingredients.isEmpty()) {
			throw new IllegalArgumentException("Ingredients cannot be null or empty");
		}
		this.setDescription(description);
		this.setQuantityCategory(quantityCategory);
		this.setQuantityValue(quantityValue);
		double calories = 0.0;
		double protein = 0.0;
		double fat = 0.0;
		double sugar = 0.0;
		double carbohydrates = 0.0;
		double sodium = 0.0;
		for (FoodItem ingredient : ingredients) {
			if (this.ingredients.containsKey(ingredient.getDescription())) {
				throw new IllegalArgumentException("Duplicate ingredients not allowed in composit foods: " + ingredient.getDescription());
			}
			this.ingredients.put(ingredient.getDescription(), ingredient);
			calories += ingredient.getCalories();
			protein += ingredient.getProtein();
			fat += ingredient.getFat();
			sugar += ingredient.getSugar();
			carbohydrates += ingredient.getCarbohydrates();
			sodium += ingredient.getSodium();
		}
		this.setCalories(calories);
		this.setProtein(protein);
		this.setFat(fat);
		this.setSugar(sugar);
		this.setCarbohydrates(carbohydrates);
		this.setSodium(sodium);
	}
	
	
	/**
	 * Gets the ingredients.
	 *
	 * @return the ingredients
	 */
	public List<FoodItem> getIngredients() {
		return this.ingredients.values().stream().toList();
	}
	
	/**
	 * Adds the ingredient.
	 * 
	 * @precondition ingredient != null && !ingredients.containsKey(ingredient.getDescription())
	 *
	 * @param ingredient the ingredient
	 * @throws IllegalArgumentException if ingredient is null or already exists in ingredients
	 */
	public void addIngredient (FoodItem ingredient) {
		if (ingredient == null) {
			throw new IllegalArgumentException("Ingredient cannot be null");
		}
		if (this.ingredients.containsKey(ingredient.getDescription())) {
			throw new IllegalArgumentException("Ingredient already exists");
		}
		this.ingredients.put(ingredient.getDescription(), ingredient);
		this.setCalories(this.getCalories() + ingredient.getCalories());
		this.setProtein(this.getProtein() + ingredient.getProtein());
		this.setFat(this.getFat() + ingredient.getFat());
		this.setSugar(this.getSugar() + ingredient.getSugar());
		this.setCarbohydrates(this.getCarbohydrates() + ingredient.getCarbohydrates());
		this.setSodium(this.getSodium() + ingredient.getSodium());
	}
	
	/**
	 * Gets the ingredient.
	 *
	 * @precondition description != null && !description.isBlank() && ingredients.containsKey(description)
	 * 
	 * @param description the description
	 * @return the ingredient
	 * @throws IllegalArgumentException if description is null or blank
	 */
	public FoodItem getIngredient(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank");
		}
		return this.ingredients.get(description);
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return this.Description;
	}

	/**
	 * Sets the description.
	 * 
	 * @precondition description != null && !description.isBlank()
	 *
	 * @param description the new description
	 * @throws IllegalArgumentException if description is null or blank
	 */
	@Override
	public void setDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank");
		}
		this.Description = description;
	}

	/**
	 * Gets the quantity category.
	 *
	 * @return the quantity category
	 */
	public QuantityCategory getQuantityCategory() {
		return this.quantityCategory;
	}

	/**
	 * Sets the quantity category.
	 * 
	 * @precondition quantityCategory != null
	 *
	 * @param quantityCategory the new quantity category
	 * @throws IllegalArgumentException if quantity category is null
	 */
	public void setQuantityCategory(QuantityCategory quantityCategory) {
		if (quantityCategory == null) {
			throw new IllegalArgumentException("Quantity category cannot be null");
		}
		this.quantityCategory = quantityCategory;
	}

	/**
	 * Gets the quantity value.
	 *
	 * @return the quantity value
	 */
	public double getQuantityValue() {
		return this.quantityValue;
	}
	
	/**
	 * Sets the quantity value.
	 * 
	 * @precondition quantityValue >= 0
	 *
	 * @param quantityValue the new quantity value
	 * @throws IllegalArgumentException if quantity value is negative
	 */
	public void setQuantityValue(double quantityValue) {
		if ( quantityValue < 0) {
			throw new IllegalArgumentException("Quantity value cannot be negative");
		}
		this.quantityValue = quantityValue;
	}

	/**
	 * Gets the calories.
	 *
	 * @return the calories
	 */
	@Override
	public double getCalories() {
		return this.calories;
	}

	private void setCalories(double calories) {
		if (calories < 0) {
			throw new IllegalArgumentException("Calories cannot be negative");
		}
		this.calories = calories;
	}

	/**
	 * Gets the protein.
	 *
	 * @return the protein
	 */
	@Override
	public double getProtein() {
		return this.protein;
	}

	private void setProtein(double protein) {
		if (protein < 0) {
			throw new IllegalArgumentException("Protein cannot be negative");
		}
		this.protein = protein;
	}

	/**
	 * Gets the fat.
	 *
	 * @return the fat
	 */
	@Override
	public double getFat() {
		return this.fat;
	}

	private void setFat(double fat) {
		if (fat < 0) {
			throw new IllegalArgumentException("Fat cannot be negative");
		}
		this.fat = fat;
	}

	/**
	 * Gets the sugar.
	 *
	 * @return the sugar
	 */
	@Override
	public double getSugar() {
		return this.sugar;
	}

	private void setSugar(double sugar) {
		if (sugar < 0) {
			throw new IllegalArgumentException("Sugar cannot be negative");
		}
		this.sugar = sugar;
	}

	/**
	 * Gets the carbohydrates.
	 *
	 * @return the carbohydrates
	 */
	@Override
	public double getCarbohydrates() {
		return this.carbohydrates;
	}

	private void setCarbohydrates(double carbohydrates) {
		if (carbohydrates < 0) {
			throw new IllegalArgumentException("Carbohydrates cannot be negative");
		}
		this.carbohydrates = carbohydrates;
	}

	/**
	 * Gets the sodium.
	 *
	 * @return the sodium
	 */
	@Override
	public double getSodium() {
		return this.sodium;
	}

	private void setSodium(double sodium) {
		if (sodium < 0) {
			throw new IllegalArgumentException("Sodium cannot be negative");
		}
		this.sodium = sodium;
	}

}
