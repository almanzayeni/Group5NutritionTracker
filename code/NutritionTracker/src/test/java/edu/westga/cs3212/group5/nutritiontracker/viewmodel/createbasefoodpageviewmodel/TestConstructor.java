package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createbasefoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateBaseFoodPageViewModel;

public class TestConstructor {
	@Test
	public void testConstructor() {
		CreateBaseFoodPageViewModel viewModel = new CreateBaseFoodPageViewModel();
		assertNotNull(viewModel.getDescriptionProperty());
		assertNotNull(viewModel.getQuantityCategoriesListProperty());
		assertNotNull(viewModel.getSelectedQuantityCategoryProperty());
		assertEquals(1.0, viewModel.getPortionSize(), 0.001);
		assertNotNull(viewModel.getCaloriesProperty());
		assertNotNull(viewModel.getProteinProperty());
		assertNotNull(viewModel.getFatProperty());
		assertNotNull(viewModel.getSugarProperty());
		assertNotNull(viewModel.getCarbohydratesProperty());
		assertNotNull(viewModel.getSodiumProperty());
	}
}
