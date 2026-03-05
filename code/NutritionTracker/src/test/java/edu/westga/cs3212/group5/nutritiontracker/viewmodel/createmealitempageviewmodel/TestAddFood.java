package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createmealitempageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;

public class TestAddFood {
	@Test
	public void testNullIngredient() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addFood(null);
		});
	}

	@Test
	public void testValidIngredient() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood food = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addFood(food);
		FoodItem result = viewModel.getFood(food);
		assertEquals(food, result);
		assertEquals(100, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(10, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(1, viewModel.getTotalSodiumProperty().get(), 0.001);
	}

	@Test
	public void testDuplicateIngredient() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood food = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addFood(food);
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addFood(food);
		});
	}
}
