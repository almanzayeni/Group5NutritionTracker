package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestAddIngredient {
	@Test
	public void testNullIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addIngredient(null);
		});
	}

	@Test
	public void testValidIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addIngredient(ingredient);
		FoodItem result = viewModel.getIngredient(ingredient);
		assertEquals(ingredient, result);
		assertEquals(100, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(10, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalSodiumProperty().get(), 0.001);
	}

	@Test
	public void testDuplicateIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addIngredient(ingredient);
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addIngredient(ingredient);
		});
	}
}
