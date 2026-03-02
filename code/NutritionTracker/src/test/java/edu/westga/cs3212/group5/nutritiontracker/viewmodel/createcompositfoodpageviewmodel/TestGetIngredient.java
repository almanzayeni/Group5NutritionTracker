package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestGetIngredient {
	@Test
	public void testNullIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.getIngredient(null);
		});
	}
	
	@Test
	public void testNonExistentIngredientWithEmptyIngredientsList() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		assertNull(viewModel.getIngredient(ingredient));
	}
	
	@Test
	public void testNonExistentIngredientWithNonEmptyIngredientsList() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
		viewModel.addIngredient(ingredient1);
		assertNull(viewModel.getIngredient(ingredient2));
	}
	
	@Test
	public void testExistingIngredientOnlyIngredient() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		BaseFood ingredient = new BaseFood("Test Ingredient", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		viewModel.addIngredient(ingredient);
		assertEquals(ingredient, viewModel.getIngredient(ingredient));
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
		assertEquals(ingredient1.getDescription(), viewModel.getIngredient(ingredient1).getDescription());
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
		assertEquals(ingredient2.getDescription(), viewModel.getIngredient(ingredient2).getDescription());
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
		assertEquals(ingredient3.getDescription(), viewModel.getIngredient(ingredient3).getDescription());
	}
}
