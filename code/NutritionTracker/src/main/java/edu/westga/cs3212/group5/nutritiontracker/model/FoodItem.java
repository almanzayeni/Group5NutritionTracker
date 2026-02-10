package edu.westga.cs3212.group5.nutritiontracker.model;

/**
 * The Interface FoodItem.
 */
public interface FoodItem {
	String getDescription();
	void setDescription(String description);
	double getCalories();
	double getProtein();
	double getFat();
	double getSugar();
	double getCarbohydrates();
	double getSodium();
}