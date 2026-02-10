package edu.westga.cs3212.group5.nutritiontracker.model;

public interface FoodItem {
	String Description = "";
	QuantityCategory quantityCategory = edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory.QUANTITY;
	double quantityValue = -1.0;
	double calories = -1.0;
	double protein = -1.0;
	double fat = -1.0;
	double sugar = -1.0;
	double carbohydrates = -1.0;
	double sodium = -1.0;
	
	String getDescription();
	void setDescription(String description);
	QuantityCategory getQuantityCategory();
	void setQuantityCategory(QuantityCategory quantityCategory);
	double getQuantityValue();
	void setQuantityValue(double quantityValue);
	double getCalories();
	void setCalories(double calories);
	double getProtein();
	void setProtein(double protein);
	double getFat();
	void setFat(double fat);
	double getSugar();
	void setSugar(double sugar);
	double getCarbohydrates();
	void setCarbohydrates(double carbohydrates);
	double getSodium();
	void setSodium(double sodium);
}