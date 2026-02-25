package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateBaseFoodPageViewModel {
	private static final String FOOD_ALREADY_EXISTS_ERROR_MESSAGE = "A food with this name already exists. Please enter a unique name, or edit the existing food item.";
	private static final String BASE_FOOD_ITEMS_JSON_FILE = "base_food_items.json";
	private StringProperty name;
	private ListProperty<QuantityCategory> quantityCategoriesList;
	private ObjectProperty<QuantityCategory> selectedQuantityCategory;
	private double portionSize;
	private DoubleProperty calories;
	private DoubleProperty protein;
	private DoubleProperty fat;
	private DoubleProperty sugar;
	private DoubleProperty carbohydrates;
	private DoubleProperty sodium;
	
	public CreateBaseFoodPageViewModel() {
		this.name = new javafx.beans.property.SimpleStringProperty();
		
		ArrayList<QuantityCategory> quantityCategories = new ArrayList<>();
		quantityCategories.add(QuantityCategory.QUANTITY);
		quantityCategories.add(QuantityCategory.WEIGHT);
		quantityCategories.add(QuantityCategory.SERVING);
		
		this.quantityCategoriesList = new javafx.beans.property.SimpleListProperty<>(FXCollections.observableArrayList(quantityCategories));
		this.selectedQuantityCategory = new javafx.beans.property.SimpleObjectProperty<>();
		this.portionSize = 1.0;
		this.calories = new javafx.beans.property.SimpleDoubleProperty();
		this.protein = new javafx.beans.property.SimpleDoubleProperty();
		this.fat = new javafx.beans.property.SimpleDoubleProperty();
		this.sugar = new javafx.beans.property.SimpleDoubleProperty();
		this.carbohydrates = new javafx.beans.property.SimpleDoubleProperty();
		this.sodium = new javafx.beans.property.SimpleDoubleProperty();
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public ListProperty<QuantityCategory> getQuantityCategoriesListProperty() {
		return quantityCategoriesList;
	}

	public ObjectProperty<QuantityCategory> getSelectedQuantityCategoryProperty() {
		return selectedQuantityCategory;
	}

	public double getPortionSizeProperty() {
		return portionSize;
	}

	public DoubleProperty getCaloriesProperty() {
		return calories;
	}

	public DoubleProperty getProteinProperty() {
		return protein;
	}

	public DoubleProperty getFatProperty() {
		return fat;
	}

	public DoubleProperty getSugarProperty() {
		return sugar;
	}

	public DoubleProperty getCarbohydratesProperty() {
		return carbohydrates;
	}

	public DoubleProperty getSodiumProperty() {
		return sodium;
	}
	
	public void createBaseFood()throws IllegalArgumentException, JsonProcessingException, IOException {
		if (this.checkForExistingFood(this.name.get())) {
			throw new IllegalArgumentException(FOOD_ALREADY_EXISTS_ERROR_MESSAGE);
		}
		
		BaseFood baseFood = new BaseFood(this.name.get(), this.selectedQuantityCategory.get(), this.portionSize, this.calories.get(), this.protein.get(), this.fat.get(), this.sugar.get(), this.carbohydrates.get(), this.sodium.get());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";
		
		try {
			jsonString = objectMapper.writeValueAsString(baseFood);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}
		
		try {
			//TODO: Send jsonString to server
			Files.write(Paths.get(BASE_FOOD_ITEMS_JSON_FILE), (jsonString + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private boolean checkForExistingFood(String foodName) {
		HashSet<String> existingFoodNames = new HashSet<>();
		try {
			Files.lines(Paths.get(BASE_FOOD_ITEMS_JSON_FILE)).forEach(line -> {
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
	
}
