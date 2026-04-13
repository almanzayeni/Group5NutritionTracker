package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestConstructor {
    private HomeDashboardViewModel vm;
    private User user;
    private DietGoals dietGoals;
    private FoodLog currentFoodLog;

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

	private BaseFood createFood(String description, double calories) {
		return new BaseFood(description, QuantityCategory.SERVING, 1, calories, 1, 1, 1, 1, 1);
	}

	private FoodLog createFoodLog() {
		List<FoodItem> breakfast = List.of(this.createFood("oatmeal", 150));
		List<FoodItem> lunch = List.of(this.createFood("salad", 250));
		List<FoodItem> dinner = List.of(this.createFood("salmon", 350));
		List<FoodItem> snacks = List.of(this.createFood("apple", 100));
		return new FoodLog(LocalDate.of(2026, 3, 25), breakfast, lunch, dinner, snacks);
	}
	
    @BeforeEach
    void setup() {
		this.dietGoals = this.createDietGoals();
		this.currentFoodLog = this.createFoodLog();
		this.user = new User("username", "password", "name", this.dietGoals, this.currentFoodLog);
        this.vm = new HomeDashboardViewModel(user);
    }
    
    @Test
    void testConstructorCopiesMealListsAndBindsInitialCalories() {
    	assertEquals(850.0, this.vm.totalCaloriesProperty().get(), 0.001);
    	
        assertEquals(1, this.vm.getBreakfastItems().size());
        assertEquals("oatmeal", this.vm.getBreakfastItems().get(0).getDescription());
        assertEquals(1, this.vm.getLunchItems().size());
        assertEquals("salad", this.vm.getLunchItems().get(0).getDescription());
        assertEquals(1, this.vm.getDinnerItems().size());
        assertEquals("salmon", this.vm.getDinnerItems().get(0).getDescription());
        assertEquals(1, this.vm.getSnacksItems().size());
        assertEquals("apple", this.vm.getSnacksItems().get(0).getDescription());
    }

    @Test
    void testConstructorInitializesUserRelatedProperties() {
    	assertSame(this.user, this.vm.getCurrentUser());
    	assertEquals("name", this.vm.getUsersNameProperty().get());
    	assertSame(this.currentFoodLog, this.vm.userFoodLogProperty().get());
    	assertSame(this.dietGoals, this.vm.userDietGoalsProperty().get());
    }
}
