package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.Collections;

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

/**
 * Tests for pending-meal-type and addFoodToPendingMeal in HomeDashboardViewModel.
 *
 * @author Group 5
 * @version Spring 2026
 */
public class TestAddFood {

    private HomeDashboardViewModel viewModel;
    private FoodItem apple;

    @BeforeEach
    void setUp() {
        DietGoals goals = new DietGoals(
                PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250,
                Collections.emptyList());
        FoodLog log = new FoodLog(LocalDate.now());
        User user = new User("testuser", "password", "Test User", goals, log);
        this.viewModel = new HomeDashboardViewModel(user);
        this.apple = new BaseFood("Apple", QuantityCategory.QUANTITY, 1, 95, 0, 0, 19, 25, 2);
    }

    // ─── setPendingMealType ───────────────────────────────────────────────────

    @Test
    void testSetPendingMealTypeNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> this.viewModel.setPendingMealType(null));
    }

    @Test
    void testSetPendingMealTypeBreakfast() {
        this.viewModel.setPendingMealType(MealType.BREAKFAST);
        assertEquals(MealType.BREAKFAST, this.viewModel.getPendingMealType());
    }

    @Test
    void testSetPendingMealTypeLunch() {
        this.viewModel.setPendingMealType(MealType.LUNCH);
        assertEquals(MealType.LUNCH, this.viewModel.getPendingMealType());
    }

    @Test
    void testSetPendingMealTypeDinner() {
        this.viewModel.setPendingMealType(MealType.DINNER);
        assertEquals(MealType.DINNER, this.viewModel.getPendingMealType());
    }

    @Test
    void testSetPendingMealTypeSnacks() {
        this.viewModel.setPendingMealType(MealType.SNACKS);
        assertEquals(MealType.SNACKS, this.viewModel.getPendingMealType());
    }

    // ─── getPendingMealType ───────────────────────────────────────────────────

    @Test
    void testGetPendingMealTypeInitiallyNull() {
        assertNull(this.viewModel.getPendingMealType());
    }

    // ─── clearPendingMealType ─────────────────────────────────────────────────

    @Test
    void testClearPendingMealType() {
        this.viewModel.setPendingMealType(MealType.DINNER);
        this.viewModel.clearPendingMealType();
        assertNull(this.viewModel.getPendingMealType());
    }

    // ─── addFoodToPendingMeal ─────────────────────────────────────────────────

    @Test
    void testAddFoodToPendingMeal_nullFood_throwsIllegalArgument() {
        this.viewModel.setPendingMealType(MealType.BREAKFAST);
        assertThrows(IllegalArgumentException.class,
                () -> this.viewModel.addFoodToPendingMeal(null));
    }

    @Test
    void testAddFoodToPendingMeal_noPendingType_throwsIllegalState() {
        assertThrows(IllegalStateException.class,
                () -> this.viewModel.addFoodToPendingMeal(this.apple));
    }

    @Test
    void testAddFoodToPendingMeal_addsToBreakfast_andClearsPending() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.BREAKFAST);
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertTrue(this.viewModel.getBreakfastItems().contains(this.apple));
            assertNull(this.viewModel.getPendingMealType());
        }
    }

    @Test
    void testAddFoodToPendingMeal_addsToLunch_andClearsPending() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.LUNCH);
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertTrue(this.viewModel.getLunchItems().contains(this.apple));
            assertNull(this.viewModel.getPendingMealType());
        }
    }

    @Test
    void testAddFoodToPendingMeal_addsToDinner_andClearsPending() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.DINNER);
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertTrue(this.viewModel.getDinnerItems().contains(this.apple));
            assertNull(this.viewModel.getPendingMealType());
        }
    }

    @Test
    void testAddFoodToPendingMeal_addsToSnacks_andClearsPending() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.SNACKS);
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertTrue(this.viewModel.getSnacksItems().contains(this.apple));
            assertNull(this.viewModel.getPendingMealType());
        }
    }

    @Test
    void testAddFoodToPendingMeal_doesNotAddToOtherMeals() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.BREAKFAST);
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertFalse(this.viewModel.getLunchItems().contains(this.apple));
            assertFalse(this.viewModel.getDinnerItems().contains(this.apple));
            assertFalse(this.viewModel.getSnacksItems().contains(this.apple));
        }
    }

    @Test
    void testAddFoodToPendingMeal_totalCaloriesUpdated() {
        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.BREAKFAST);
            double before = this.viewModel.totalCaloriesProperty().get();
            this.viewModel.addFoodToPendingMeal(this.apple);

            assertEquals(before + this.apple.getCalories(),
                    this.viewModel.totalCaloriesProperty().get(), 0.001);
        }
    }

    @Test
    void testAddMultipleFoodsToSameMeal() {
        FoodItem banana = new BaseFood("Banana", QuantityCategory.QUANTITY,
                1, 105, 1, 0, 14, 27, 1);

        try (MockedStatic<UpdateFoodLogRequestHandler> mockHandler = this.mockUpdateHandler()) {
            this.viewModel.setPendingMealType(MealType.LUNCH);
            this.viewModel.addFoodToPendingMeal(this.apple);

            this.viewModel.setPendingMealType(MealType.LUNCH);
            this.viewModel.addFoodToPendingMeal(banana);

            assertEquals(2, this.viewModel.getLunchItems().size());
            assertTrue(this.viewModel.getLunchItems().contains(this.apple));
            assertTrue(this.viewModel.getLunchItems().contains(banana));
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
