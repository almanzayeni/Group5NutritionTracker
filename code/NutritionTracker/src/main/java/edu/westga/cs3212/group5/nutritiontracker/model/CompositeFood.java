package edu.westga.cs3212.group5.nutritiontracker.model;

import java.util.HashMap;
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
	private double portionSize;
	private double calories;
	private double protein;
	private double fat;
	private double sugar;
	private double carbohydrates;
	private double sodium;
	private Map<String, FoodItem> ingredients;
	
	/**
	 * Instantiates a new composite food.
	 */
	public CompositeFood() {}
	
	/**
	 * Instantiates a new composite food.
	 *
	 * @precondition description != null && !description.isBlank() && quantityCategory != null && portionSize >= 1 && ingredients != null && !ingredients.isEmpty() && no duplicate descriptions in ingredients
	 *
	 * @param description the description
	 * @param quantityCategory the quantity category
	 * @param portionSize portion size
	 * @param ingredients the ingredients
	 * @param calories the calories
	 * @param protein the protein
	 * @param fat the fat
	 * @param sugar the sugar
	 * @param carbohydrates the carbohydrates
	 * @param sodium the sodium
	 * @throws IllegalArgumentException if description is null or blank, quantity category is null, portionSize is less than 1, or ingredients is null or empty or contains duplicate descriptions
	 */
	public CompositeFood(String description, QuantityCategory quantityCategory, double portionSize, List<FoodItem> ingredients, double calories, double protein, double fat, double sugar, double carbohydrates, double sodium) {
		if (ingredients == null || ingredients.isEmpty()) {
			throw new IllegalArgumentException("Ingredients cannot be null or empty");
		}
		
		this.ingredients = new HashMap<String, FoodItem>();
		this.setDescription(description);
		this.setQuantityCategory(quantityCategory);
		this.setPortionSize(portionSize);
		
		for (FoodItem ingredient : ingredients) {
			if (this.ingredients.containsKey(ingredient.getDescription())) {
				throw new IllegalArgumentException("Duplicate ingredients not allowed in composit foods: " + ingredient.getDescription());
			}
			this.ingredients.put(ingredient.getDescription(), ingredient);
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
	 * @param ingredient the ingredient to add
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
	 * Gets the ingredient with the given description.
	 *
	 * @precondition description != null && !description.isBlank() && ingredients.containsKey(description)
	 * 
	 * @param description the description of the ingredient to get
	 * @return the ingredient with the given description or null if no such ingredient exists
	 * @throws IllegalArgumentException if description is null or blank
	 */
	public FoodItem getIngredientByDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank");
		}
		return this.ingredients.get(description);
	}
	
	/**
	 * Removes the ingredient with the given description.
	 *
	 * @precondition description != null && !description.isBlank()
	 * 
	 * @param description the description of the ingredient to remove
	 * @return true if the ingredient was removed, false if no such ingredient exists
	 * @throws IllegalArgumentException if description is null or blank
	 */
	public boolean removeIngredientByDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or blank");
		}
		FoodItem ingredient = this.ingredients.remove(description);
		if (ingredient != null) {
			this.setCalories(this.getCalories() - ingredient.getCalories());
			this.setProtein(this.getProtein() - ingredient.getProtein());
			this.setFat(this.getFat() - ingredient.getFat());
			this.setSugar(this.getSugar() - ingredient.getSugar());
			this.setCarbohydrates(this.getCarbohydrates() - ingredient.getCarbohydrates());
			this.setSodium(this.getSodium() - ingredient.getSodium());
			return true;
		}
		return false;
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
	 * Gets the portion size.
	 *
	 * @return the portion size
	 */
	@Override
	public double getPortionSize() {
		return this.portionSize;
	}
	
	/**
	 * Sets the portion size.
	 * 
	 * @precondition portionSize >= 1
	 *
	 * @param portionSize the new quantity value
	 * @throws IllegalArgumentException if quantity value is less than 1
	 */
	public void setPortionSize(double portionSize) {
		if ( portionSize < 1) {
			throw new IllegalArgumentException("Portion size must be 1 or greater");
		}
		this.portionSize = portionSize;
	}

	/**
	 * Gets the calories.
	 *
	 * @return the calories
	 */
	@Override
	public double getCalories() {
		return this.calories * this.portionSize;
	}

	private void setCalories(double calories) {
		this.calories = calories;
	}

	/**
	 * Gets the protein.
	 *
	 * @return the protein
	 */
	@Override
	public double getProtein() {
		return this.protein * this.portionSize;
	}

	private void setProtein(double protein) {
		this.protein = protein;
	}

	/**
	 * Gets the fat.
	 *
	 * @return the fat
	 */
	@Override
	public double getFat() {
		return this.fat * this.portionSize;
	}

	private void setFat(double fat) {
		this.fat = fat;
	}

	/**
	 * Gets the sugar.
	 *
	 * @return the sugar
	 */
	@Override
	public double getSugar() {
		return this.sugar * this.portionSize;
	}

	private void setSugar(double sugar) {
		this.sugar = sugar;
	}

	/**
	 * Gets the carbohydrates.
	 *
	 * @return the carbohydrates
	 */
	@Override
	public double getCarbohydrates() {
		return this.carbohydrates * this.portionSize;
	}

	private void setCarbohydrates(double carbohydrates) {
		this.carbohydrates = carbohydrates;
	}

	/**
	 * Gets the sodium.
	 *
	 * @return the sodium
	 */
	@Override
	public double getSodium() {
		return this.sodium * this.portionSize;
	}

	private void setSodium(double sodium) {
		this.sodium = sodium;
	}

}
