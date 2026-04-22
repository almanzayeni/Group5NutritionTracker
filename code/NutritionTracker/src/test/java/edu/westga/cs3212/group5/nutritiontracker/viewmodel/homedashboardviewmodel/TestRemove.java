package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.MealType;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.UpdateFoodLogRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestRemove {

    private HomeDashboardViewModel viewModel;
    private BaseFood breakfastFood;
    private BaseFood lunchFood;
    private BaseFood dinnerFood;
    private BaseFood snackFood;

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

	private BaseFood createFood(String description, double calories) {
		return new BaseFood(description, QuantityCategory.SERVING, 1, calories, 1, 1, 1, 1, 1);
	}

    @BeforeEach
    void setup() {
    	this.breakfastFood = this.createFood("breakfast", 100);
    	this.lunchFood = this.createFood("lunch", 200);
    	this.dinnerFood = this.createFood("dinner", 300);
    	this.snackFood = this.createFood("snack", 50);

    	List<FoodItem> breakfast = new ArrayList<FoodItem>(List.of(this.breakfastFood));
    	List<FoodItem> lunch = new ArrayList<FoodItem>(List.of(this.lunchFood));
    	List<FoodItem> dinner = new ArrayList<FoodItem>(List.of(this.dinnerFood));
    	List<FoodItem> snacks = new ArrayList<FoodItem>(List.of(this.snackFood));
    	FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 3, 25), breakfast, lunch, dinner, snacks);
    	User user = new User("username", "password", "name", this.createDietGoals(), currentFoodLog);

        this.viewModel = new HomeDashboardViewModel(user);
    }

    @Test
    void testRemoveFoodFromSnacksUpdatesLogAndObservableList() {
    	try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
    		this.viewModel.removeFoodFromMeal(this.snackFood, MealType.SNACKS);

            assertEquals(0, this.viewModel.getCurrentUser().getCurrentFoodLog().getSnacks().size());
            assertEquals(0, this.viewModel.getSnacksItems().size());
    	}
    }

    @Test
    void testRemoveFoodFromBreakfastUpdatesLogAndObservableList() {
    	try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
    		this.viewModel.removeFoodFromMeal(this.breakfastFood, MealType.BREAKFAST);

            assertEquals(0, this.viewModel.getCurrentUser().getCurrentFoodLog().getBreakfast().size());
            assertEquals(0, this.viewModel.getBreakfastItems().size());
    	}
    }

    @Test
    void testRemoveFoodFromLunchUpdatesLogAndObservableList() {
    	try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
    		this.viewModel.removeFoodFromMeal(this.lunchFood, MealType.LUNCH);

            assertEquals(0, this.viewModel.getCurrentUser().getCurrentFoodLog().getLunch().size());
            assertEquals(0, this.viewModel.getLunchItems().size());
    	}
    }

    @Test
    void testRemoveFoodFromDinnerUpdatesLogAndObservableList() {
    	try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
    		this.viewModel.removeFoodFromMeal(this.dinnerFood, MealType.DINNER);

            assertEquals(0, this.viewModel.getCurrentUser().getCurrentFoodLog().getDinner().size());
            assertEquals(0, this.viewModel.getDinnerItems().size());
    	}
    }

    @Test
    void testRemoveFoodFromMealUpdatesTotalCaloriesWithoutTouchingOtherMeals() {
    	double before = this.viewModel.totalCaloriesProperty().get();

    	try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
    		this.viewModel.removeFoodFromMeal(this.breakfastFood, MealType.BREAKFAST);

            assertEquals(before - this.breakfastFood.getCalories(), this.viewModel.totalCaloriesProperty().get(), 0.001);
            assertFalse(this.viewModel.getLunchItems().isEmpty());
            assertFalse(this.viewModel.getDinnerItems().isEmpty());
            assertFalse(this.viewModel.getSnacksItems().isEmpty());
    	}
    }

    private MockedStatic<UpdateFoodLogRequestHandler> mockUpdateHandler() {
    	MockedStatic<UpdateFoodLogRequestHandler> mockHandler = mockStatic(UpdateFoodLogRequestHandler.class);
    	mockHandler.when(() -> UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(this.viewModel.getCurrentUser()))
    			.thenReturn("{\"request_type\":\"UPDATE_FOODLOG\"}");
    	mockHandler.when(() -> UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest("{\"request_type\":\"UPDATE_FOODLOG\"}"))
    			.thenAnswer(invocation -> null);
    	return mockHandler;
    }
}
