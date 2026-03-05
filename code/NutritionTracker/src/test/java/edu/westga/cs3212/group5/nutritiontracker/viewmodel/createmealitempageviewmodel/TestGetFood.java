package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createmealitempageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;

public class TestGetFood {
	@Test
	public void testNullIngredient() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.getFood(null);
		});
	}

	@Test
	public void testNonExistentIngredientWithEmptyIngredientsList() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		assertNull(viewModel.getFood(ingredient));
	}

	@Test
	public void testNonExistentIngredientWithNonEmptyIngredientsList() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		viewModel.addFood(ingredient1);
		assertNull(viewModel.getFood(ingredient2));
	}

	@Test
	public void testExistingIngredientOnlyIngredient() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addFood(ingredient);
		assertEquals(ingredient, viewModel.getFood(ingredient));
	}

	@Test
	public void testExistingIngredientFirstPositionMultipleIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addFood(ingredient1);
		viewModel.addFood(ingredient2);
		viewModel.addFood(ingredient3);
		assertEquals(ingredient1.getDescription(), viewModel.getFood(ingredient1).getDescription());
	}

	@Test
	public void testExistingIngredientMiddlePositionMultipleIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addFood(ingredient1);
		viewModel.addFood(ingredient2);
		viewModel.addFood(ingredient3);
		assertEquals(ingredient2.getDescription(), viewModel.getFood(ingredient2).getDescription());
	}

	@Test
	public void testExistingIngredientLastPositionMultipleIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		BaseFood ingredient3 = new BaseFood("Ingredient 3", QuantityCategory.SERVING, 1, 300, 30, 3, 3, 3, 3);
		viewModel.addFood(ingredient1);
		viewModel.addFood(ingredient2);
		viewModel.addFood(ingredient3);
		assertEquals(ingredient3.getDescription(), viewModel.getFood(ingredient3).getDescription());
	}
}
