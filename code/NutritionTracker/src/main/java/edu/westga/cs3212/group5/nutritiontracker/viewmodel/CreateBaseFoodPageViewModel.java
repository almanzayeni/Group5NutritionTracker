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
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateBaseFoodPageViewModel {
	private static final String FOOD_ALREADY_EXISTS_ERROR_MESSAGE = "A food with this name already exists. Please enter a unique name, or edit the existing food.";
	private static final String BASE_FOOD_ITEMS_JSON_FILE = "base_food_items.json";
	private StringProperty description;
	private ListProperty<QuantityCategory> quantityCategoriesList;
	private ObjectProperty<QuantityCategory> selectedQuantityCategory;
	private double portionSize;
	private DoubleProperty calories;
	private DoubleProperty protein;
	private DoubleProperty fat;
	private DoubleProperty sugar;
	private DoubleProperty carbohydrates;
	private DoubleProperty sodium;
	private String filePath;
	private ObjectMapper objectMapper;

	/**
	 * Instantiates a new creates the base food page view model.
	 */
	public CreateBaseFoodPageViewModel() {
		this.description = new javafx.beans.property.SimpleStringProperty();

		ArrayList<QuantityCategory> quantityCategories = new ArrayList<>();
		quantityCategories.add(QuantityCategory.QUANTITY);
		quantityCategories.add(QuantityCategory.WEIGHT);
		quantityCategories.add(QuantityCategory.SERVING);

		this.quantityCategoriesList = new SimpleListProperty<QuantityCategory>(
				FXCollections.observableArrayList(quantityCategories));
		this.selectedQuantityCategory = new SimpleObjectProperty<QuantityCategory>();
		this.portionSize = 1.0;
		this.calories = new javafx.beans.property.SimpleDoubleProperty();
		this.protein = new javafx.beans.property.SimpleDoubleProperty();
		this.fat = new javafx.beans.property.SimpleDoubleProperty();
		this.sugar = new javafx.beans.property.SimpleDoubleProperty();
		this.carbohydrates = new javafx.beans.property.SimpleDoubleProperty();
		this.sodium = new javafx.beans.property.SimpleDoubleProperty();
		this.filePath = BASE_FOOD_ITEMS_JSON_FILE;
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Instantiates a new creates the base food page view model. FOR TESTING
	 * PURPOSES ONLY - allows injection of a custom file path to simulate file I/O
	 * exceptions.
	 *
	 * @param filePath the file path
	 */
	public CreateBaseFoodPageViewModel(String filePath) {
		this();
		this.filePath = filePath;
	}

	/**
	 * Instantiates a new creates the base food page view model. FOR TESTING
	 * PURPOSES ONLY - allows injection of a mock ObjectMapper to simulate JSON
	 * processing exceptions.
	 *
	 * @param objectMapper the object mapper
	 */
	public CreateBaseFoodPageViewModel(ObjectMapper objectMapper) {
		this();
		this.objectMapper = objectMapper;
	}

	/**
	 * Gets the name property.
	 *
	 * @return the name property
	 */
	public StringProperty getDescriptionProperty() {
		return description;
	}

	/**
	 * Gets the quantity categories list property.
	 *
	 * @return the quantity categories list property
	 */
	public ListProperty<QuantityCategory> getQuantityCategoriesListProperty() {
		return quantityCategoriesList;
	}

	/**
	 * Gets the selected quantity category property.
	 *
	 * @return the selected quantity category property
	 */
	public ObjectProperty<QuantityCategory> getSelectedQuantityCategoryProperty() {
		return selectedQuantityCategory;
	}

	/**
	 * Gets the portion size.
	 *
	 * @return the portion size
	 */
	public double getPortionSize() {
		return portionSize;
	}

	/**
	 * Gets the calories property.
	 *
	 * @return the calories property
	 */
	public DoubleProperty getCaloriesProperty() {
		return calories;
	}

	/**
	 * Gets the protein property.
	 *
	 * @return the protein property
	 */
	public DoubleProperty getProteinProperty() {
		return protein;
	}

	/**
	 * Gets the fat property.
	 *
	 * @return the fat property
	 */
	public DoubleProperty getFatProperty() {
		return fat;
	}

	/**
	 * Gets the sugar property.
	 *
	 * @return the sugar property
	 */
	public DoubleProperty getSugarProperty() {
		return sugar;
	}

	/**
	 * Gets the carbohydrates property.
	 *
	 * @return the carbohydrates property
	 */
	public DoubleProperty getCarbohydratesProperty() {
		return carbohydrates;
	}

	/**
	 * Gets the sodium property.
	 *
	 * @return the sodium property
	 */
	public DoubleProperty getSodiumProperty() {
		return sodium;
	}

	/**
	 * Creates a new the base food and saves it.
	 *
	 * @throws IllegalArgumentException if a food with the same name already exists
	 * @throws JsonProcessingException  if there is an error processing the JSON
	 *                                  data
	 * @throws IOException              Signals that an I/O exception has occurred.
	 */
	public void createBaseFood() throws IllegalArgumentException, JsonProcessingException, IOException {
		if (this.checkForExistingFood(this.description.get())) {
			throw new IllegalArgumentException(FOOD_ALREADY_EXISTS_ERROR_MESSAGE);
		}

		BaseFood baseFood = new BaseFood(this.description.get(), this.selectedQuantityCategory.get(), this.portionSize,
				this.calories.get(), this.protein.get(), this.fat.get(), this.sugar.get(), this.carbohydrates.get(),
				this.sodium.get());
		String jsonString = "";

		try {
			jsonString = this.objectMapper.writeValueAsString(baseFood);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}

		try {
			// TODO: Send jsonString to server
			Files.write(Paths.get(this.filePath), (jsonString + System.lineSeparator()).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private boolean checkForExistingFood(String foodName) {
		HashSet<String> existingFoodNames = new HashSet<>();
		try {
			Files.lines(Paths.get(this.filePath)).forEach(line -> {
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
