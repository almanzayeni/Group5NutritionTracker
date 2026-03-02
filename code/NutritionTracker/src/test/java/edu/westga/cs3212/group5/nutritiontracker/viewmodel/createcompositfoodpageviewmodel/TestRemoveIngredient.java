package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestRemoveIngredient {
	@Test
	public void testNullIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.removeIngredient(null);
		});
	}
	
	@Test
	public void testNonExistentIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.removeIngredient(ingredient);
		});
	}
	
	@Test
	public void testExistingIngredientOnlyIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addIngredient(ingredient);
		viewModel.removeIngredient(ingredient);
		assertNull(viewModel.getIngredient(ingredient));
		assertEquals(0, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(0, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(0, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(0, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(0, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(0, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
	
	@Test
	public void testExistingIngredientFirstPositionMultipleIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addIngredient(ingredient1);
		viewModel.addIngredient(ingredient2);
		viewModel.addIngredient(ingredient3);
		viewModel.removeIngredient(ingredient1);
		assertNull(viewModel.getIngredient(ingredient1));
		assertEquals(500, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(50, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(5, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(5, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(5, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(5, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
	
	@Test
	public void testExistingIngredientMiddlePositionMultipleIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addIngredient(ingredient1);
		viewModel.addIngredient(ingredient2);
		viewModel.addIngredient(ingredient3);
		viewModel.removeIngredient(ingredient2);
		assertNull(viewModel.getIngredient(ingredient2));
		assertEquals(400, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(40, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(4, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(4, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(4, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(4, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
	
	@Test
	public void testExistingIngredientLastPositionMultipleIngredients() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addIngredient(ingredient1);
		viewModel.addIngredient(ingredient2);
		viewModel.addIngredient(ingredient3);
		viewModel.removeIngredient(ingredient3);
		assertNull(viewModel.getIngredient(ingredient3));
		assertEquals(300, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(30, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(3, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(3, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(3, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(3, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
}
