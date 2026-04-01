package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestConstructor {
    HomeDashboardViewModel vm;
    
	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

	private FoodLog createFoodLog() {
		return new FoodLog(LocalDate.of(2026, 3, 25));
	}
	
    @BeforeEach
    void setup() {
		DietGoals dietGoals = this.createDietGoals();
		FoodLog currentFoodLog = this.createFoodLog();
		User user = new User("username", "password", "name", dietGoals, currentFoodLog);
        this.vm = new HomeDashboardViewModel(user);
    }
    
    @Test
    void testConstructorBindsCalories() {
    	double totalCalories = 0;
    	assertEquals(totalCalories, this.vm.totalCaloriesProperty().get());
    	
        assertEquals(0, vm.getBreakfastItems().size());
        assertEquals(0, vm.getLunchItems().size());
        assertEquals(0, vm.getDinnerItems().size());
        assertEquals(0, vm.getSnacksItems().size());
    }
}
