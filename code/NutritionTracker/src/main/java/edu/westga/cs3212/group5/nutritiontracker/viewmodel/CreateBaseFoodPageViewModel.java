package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import java.util.ArrayList;

import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CreateBaseFoodPageViewModel {
	private StringProperty name;
	private ListProperty<QuantityCategory> quantityCategoriesList;
	private ObjectProperty<QuantityCategory> selectedQuantityCategory;
	private DoubleProperty portionSize;
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
		this.portionSize = new javafx.beans.property.SimpleDoubleProperty();
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

	public DoubleProperty getPortionSizeProperty() {
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
	
	
}
