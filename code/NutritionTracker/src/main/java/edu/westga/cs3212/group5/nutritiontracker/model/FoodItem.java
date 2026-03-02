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
	String getDescription();

	void setDescription(String description);

	double getPortionSize();

	double getCalories();

	double getProtein();

	double getFat();

	double getSugar();

	double getCarbohydrates();

	double getSodium();
}