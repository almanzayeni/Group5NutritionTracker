package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestAddIngredients {
	@Test
	public void testNullIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addIngredients(null);
		});
	}

	@Test
	public void testEmptyIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addIngredients(new ArrayList<FoodItem>());
		});
	}

	@Test
	public void testValidIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		ArrayList<FoodItem> ingredients = new ArrayList<>();
		ingredients.add(ingredient1);
		CompositeFood ingredient2 = new CompositeFood("Ingredient 2", QuantityCategory.QUANTITY, 1, ingredients);
		ingredients.add(ingredient2);
		viewModel.addIngredients(ingredients);
		assertEquals(200, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(20, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
}
