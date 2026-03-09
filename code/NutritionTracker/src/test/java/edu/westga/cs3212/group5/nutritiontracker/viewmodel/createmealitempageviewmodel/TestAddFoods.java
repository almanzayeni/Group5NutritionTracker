package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createmealitempageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;


public class TestAddFoods {
	@Test
	public void testNullIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addFoods(null);
		});
	}

	@Test
	public void testEmptyIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		assertThrows(IllegalArgumentException.class, () -> {
			viewModel.addFoods(new ArrayList<FoodItem>());
		});
	}

	@Test
	public void testValidIngredients() {
		CreateMealItemPageViewModel viewModel = new CreateMealItemPageViewModel();
		BaseFood food1 = new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
		ArrayList<FoodItem> foods = new ArrayList<>();
		foods.add(food1);
		CompositeFood food2 = new CompositeFood("Ingredient 2", QuantityCategory.QUANTITY, 1, foods);
		foods.add(food2);
		viewModel.addFoods(foods);
		assertEquals(200, viewModel.getTotalCaloriesProperty().get(), 0.001);
		assertEquals(20, viewModel.getTotalProteinProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalFatProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalSugarProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalCarbohydratesProperty().get(), 0.001);
		assertEquals(2, viewModel.getTotalSodiumProperty().get(), 0.001);
	}
}
