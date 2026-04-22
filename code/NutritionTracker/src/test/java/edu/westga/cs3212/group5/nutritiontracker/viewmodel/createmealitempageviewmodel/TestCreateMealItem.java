package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createmealitempageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;

public class TestCreateMealItem {

	private BaseFood food1() {
		return new BaseFood("Food 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
	}

	private BaseFood food2() {
		return new BaseFood("Food 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
	}

	private CreateMealItemPageViewModel buildValidVm() {
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel();
		vm.getDescriptionProperty().set("Test Meal");
		vm.addFood(food1());
		vm.addFood(food2());
		return vm;
	}

	@Test
	void testcreateMealItemNullNameThrowsIllegalArgumentException() {
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel();
		vm.getDescriptionProperty().set(null);

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemEmptyNameThrowsIllegalArgumentException() {
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel();
		vm.getDescriptionProperty().set("");

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemNoIngredientsThrowsIllegalArgumentException() {
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel();
		vm.getDescriptionProperty().set("Test Meal");

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testCreateValidMealSuccessAndFieldsCleared() throws Exception {
		CreateMealItemPageViewModel vm = this.buildValidVm();

		vm.createMealItem();

		assertEquals("", vm.getDescriptionProperty().get());
		assertTrue(vm.getIngredientsListProperty().isEmpty());
		assertEquals(0.0, vm.getTotalCaloriesProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalProteinProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalFatProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalSugarProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalCarbohydratesProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalSodiumProperty().get(), 0.0001);
	}
}
