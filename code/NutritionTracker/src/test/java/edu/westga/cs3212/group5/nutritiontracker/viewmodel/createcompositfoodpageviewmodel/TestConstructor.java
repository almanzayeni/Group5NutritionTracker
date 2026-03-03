package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestConstructor {
	@Test
	public void testConstructor() {
		CreateCompositeFoodPageViewModel viewModel = new CreateCompositeFoodPageViewModel();
		assertNotNull(viewModel.getNameProperty());
		assertNotNull(viewModel.getQuantityCategoriesListPropery());
		assertNotNull(viewModel.getSelectedQuantityCategoryProperty());
		assertEquals(1.0, viewModel.getPortionSize(), 0.001);
		assertNotNull(viewModel.getTotalCaloriesProperty());
		assertNotNull(viewModel.getTotalProteinProperty());
		assertNotNull(viewModel.getTotalFatProperty());
		assertNotNull(viewModel.getTotalSugarProperty());
		assertNotNull(viewModel.getTotalCarbohydratesProperty());
		assertNotNull(viewModel.getTotalSodiumProperty());
		assertNotNull(viewModel.getIngredientsListProperty());
	}
}
