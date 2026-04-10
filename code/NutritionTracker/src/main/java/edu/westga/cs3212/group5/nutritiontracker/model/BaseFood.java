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
	private double portionSize;
	private double calories;
	private double protein;
	private double fat;
	private double sugar;
	private double carbohydrates;
	private double sodium;

	/**
	 * Instantiates a new base food.
	 */
	public BaseFood() {
	}

	/**
	 * Instantiates a new base food.
	 * 
	 * @precondition description != null && !description.isBlank() &&
	 *               quantityCategory != null && portionSize > 1 && calories >= 0 &&
	 *               protein >= 0 && fat >= 0 && sugar >= 0 && carbohydrates >= 0 &&
	 *               sodium >= 0
	 *
	 * @param description      the description
	 * @param quantityCategory the quantity category
	 * @param calories         the calories
	 * @param protein          the protein
	 * @param fat              the fat
	 * @param sugar            the sugar
	 * @param carbohydrates    the carbohydrates
	 * @param sodium           the sodium
	 * @throws IllegalArgumentException if description is null or blank, quantity
	 *                                  category is null, portion size is less than
	 *                                  1, calories is negative, protein is
	 *                                  negative, fat is negative, sugar is
	 *                                  negative, carbohydrates is negative, or
	 *                                  sodium is negative
	 */
	public BaseFood(String description, QuantityCategory quantityCategory, double portionSize, double calories,
			double protein, double fat, double sugar, double carbohydrates, double sodium) {
		this.setDescription(description);
		this.setQuantityCategory(quantityCategory);
		this.setPortionSize(portionSize);
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
	@Override
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
	@Override
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
	 * @param portionSize the new portion size
	 * @throws IllegalArgumentException if portion size is not positive
	 */
	@Override
	public void setPortionSize(double portionSize) {
		if (portionSize < 1) {
			throw new IllegalArgumentException("Portion size must 1 or greater");
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

	/**
	 * Sets the calories.
	 * 
	 * @precondition calories >= 0
	 *
	 * @param calories the new calories
	 * @throws IllegalArgumentException if calories is negative
	 */
	public void setCalories(double calories) {
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
		return this.protein * this.portionSize;
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
		return this.fat * this.portionSize;
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
		return this.sugar * this.portionSize;
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
		return this.carbohydrates * this.portionSize;
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
		return this.sodium * this.portionSize;
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
