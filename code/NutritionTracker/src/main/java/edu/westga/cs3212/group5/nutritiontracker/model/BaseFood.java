package edu.westga.cs3212.group5.nutritiontracker.model;

/**
 * The Class BaseFood.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class BaseFood implements FoodItem {
	private String Description;
	private QuantityCategory quantityCategory;
	private double quantityValue;
	private double calories;
	private double protein = 0.0;
	private double fat = 0.0;
	private double sugar = 0.0;
	private double carbohydrates = 0.0;
	private double sodium = 0.0;
	
	/**
	 * Instantiates a new base food.
	 *
	 *@precondition description != null && !description.isBlank() && quantityCategory != null && quantityValue >= 0 && calories >= 0
	 *
	 * @param description the description
	 * @param quantityCategory the quantity category
	 * @param quantityValue the quantity value
	 * @param calories the calories
	 * @throws IllegalArgumentException if description is null or blank, quantity category is null, quantity value is negative, or calories is negative
	 */
	public BaseFood(String description, QuantityCategory quantityCategory, double quantityValue, double calories) {
		this.setDescription(description);
		this.setQuantityCategory(quantityCategory);
		this.setQuantityValue(quantityValue);
		this.setCalories(calories);
	}
	
	/**
	 * Instantiates a new base food.
	 * 
	 * @precondition description != null && !description.isBlank() && quantityCategory != null && quantityValue >= 0 && calories >= 0 && protein >= 0 && fat >= 0 && sugar >= 0 && carbohydrates >= 0 && sodium >= 0
	 *
	 * @param description the description
	 * @param quantityCategory the quantity category
	 * @param quantityValue the quantity value
	 * @param calories the calories
	 * @param protein the protein
	 * @param fat the fat
	 * @param sugar the sugar
	 * @param carbohydrates the carbohydrates
	 * @param sodium the sodium
	 * @throws IllegalArgumentException if description is null or blank, quantity category is null, quantity value is negative, calories is negative, protein is negative, fat is negative, sugar is negative, carbohydrates is negative, or sodium is negative
	 */
	public BaseFood(String description, QuantityCategory quantityCategory, double quantityValue, double calories,
			double protein, double fat, double sugar, double carbohydrates, double sodium) {
		this.setDescription(description);
		this.setQuantityCategory(quantityCategory);
		this.setQuantityValue(quantityValue);
		this.setCalories(calories);
		this.setProtein(protein);
		this.setFat(fat);
		this.setSugar(sugar);
		this.setCarbohydrates(carbohydrates);
		this.setSodium(sodium);
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
		if ( quantityValue <= 0) {
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

	/**
	 * Sets the calories.
	 * 
	 * @precondition calories >= 0
	 *
	 * @param calories the new calories
	 * @throws IllegalArgumentException if calories is negative
	 */
	public void setCalories(double calories) {
		if (calories <= 0) {
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

	/**
	 * Sets the protein.
	 * 
	 * @precondition protein > 0
	 *
	 * @param protein the new protein
	 * @throws IllegalArgumentException if protein is negative
	 */
	public void setProtein(double protein) {
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

	/**
	 * Sets the fat.
	 * 
	 * @precondition fat >= 0
	 *
	 * @param fat the new fat
	 * @throws IllegalArgumentException if fat is negative
	 */
	public void setFat(double fat) {
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

	/**
	 * Sets the sugar.
	 * 
	 * @precondition sugar >= 0
	 *
	 * @param sugar the new sugar
	 * @throws IllegalArgumentException if sugar is negative
	 */
	public void setSugar(double sugar) {
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

	/**
	 * Sets the carbohydrates.
	 * 
	 * @precondition carbohydrates >= 0
	 *
	 * @param carbohydrates the new carbohydrates
	 * @throws IllegalArgumentException if carbohydrates is negative
	 */
	public void setCarbohydrates(double carbohydrates) {
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

	/**
	 * Sets the sodium.
	 * 
	 * @precondition sodium >= 0
	 *
	 * @param sodium the new sodium
	 * @throws IllegalArgumentException if sodium is negative
	 */
	public void setSodium(double sodium) {
		if (sodium < 0) {
			throw new IllegalArgumentException("Sodium cannot be negative");
		}
		this.sodium = sodium;
	}

}
