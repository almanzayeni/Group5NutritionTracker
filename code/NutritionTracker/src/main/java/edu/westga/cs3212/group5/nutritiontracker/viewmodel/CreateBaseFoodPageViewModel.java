package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.util.ArrayList;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Create base food view model.
 * 
 * @author Justin, Yeni
 * @version Spring 2026
 */
public class CreateBaseFoodPageViewModel {
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

	/**
	 * Instantiates a new creates the base food page view model.
	 */
	public CreateBaseFoodPageViewModel() {
		this.description = new SimpleStringProperty();

		ArrayList<QuantityCategory> quantityCategories = new ArrayList<>();
		quantityCategories.add(QuantityCategory.QUANTITY);
		quantityCategories.add(QuantityCategory.WEIGHT);
		quantityCategories.add(QuantityCategory.SERVING);

		this.quantityCategoriesList = new SimpleListProperty<QuantityCategory>(
				FXCollections.observableArrayList(quantityCategories));
		this.selectedQuantityCategory = new SimpleObjectProperty<QuantityCategory>();
		this.portionSize = 1.0;
		this.calories = new SimpleDoubleProperty();
		this.protein = new SimpleDoubleProperty();
		this.fat = new SimpleDoubleProperty();
		this.sugar = new SimpleDoubleProperty();
		this.carbohydrates = new SimpleDoubleProperty();
		this.sodium = new SimpleDoubleProperty();
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
	 * Creates a new base food and sends it to the server.
	 *
	 * @throws IllegalArgumentException if a food with the same name already exists
	 * @throws RuntimeException         if the server call fails
	 */
	public void createBaseFood() {
		BaseFood baseFood = new BaseFood(
				this.description.get(),
				this.selectedQuantityCategory.get(),
				this.portionSize,
				this.calories.get(),
				this.protein.get(),
				this.fat.get(),
				this.sugar.get(),
				this.carbohydrates.get(),
				this.sodium.get());

		String request = AddFoodRequestHandler.createAddFoodRequest(baseFood);
		AddFoodRequestHandler.handleAddFoodRequest(request);
	}
}