package edu.westga.cs3212.group5.nutritiontracker.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = CompositeFood.class, name = "composite"),
		@JsonSubTypes.Type(value = BaseFood.class, name = "base") })

/**
 * The Interface FoodItem.
 */
public interface FoodItem {
	public static final String FOOD_ITEMS_JSON_FILE = "food_items.json";
	
	String getDescription();

	void setDescription(String description);
	
	QuantityCategory getQuantityCategory();
	
	void setQuantityCategory(QuantityCategory quantityCategory);

	double getPortionSize();
	
	void setPortionSize(double portionSize);

	double getCalories();

	double getProtein();

	double getFat();

	double getSugar();

	double getCarbohydrates();

	double getSodium();
}