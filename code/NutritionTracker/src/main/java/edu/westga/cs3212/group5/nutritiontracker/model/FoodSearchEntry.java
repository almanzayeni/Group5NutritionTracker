package edu.westga.cs3212.group5.nutritiontracker.model;

/**
 * A lightweight food record read directly from food_search_data.json.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodSearchEntry {

	private String description;
	private double calories;
	private double protein;
	private double fat;
	private double sugar;
	private double carbohydrates;
	private double sodium;

	public FoodSearchEntry() {
	}

	public String getDescription() {
		return this.description;
	}

	public double getCalories() {
		return this.calories;
	}

	public double getProtein() {
		return this.protein;
	}

	public double getFat() {
		return this.fat;
	}

	public double getSugar() {
		return this.sugar;
	}

	public double getCarbohydrates() {
		return this.carbohydrates;
	}

	public double getSodium() {
		return this.sodium;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCalories(double calories) {
		this.calories = calories;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setSugar(double sugar) {
		this.sugar = sugar;
	}

	public void setCarbohydrates(double carbohydrates) {
		this.carbohydrates = carbohydrates;
	}

	public void setSodium(double sodium) {
		this.sodium = sodium;
	}

	@Override
	public String toString() {
		return this.description + "  —  " + String.format("%.0f", this.calories) + " cal";
	}
}
