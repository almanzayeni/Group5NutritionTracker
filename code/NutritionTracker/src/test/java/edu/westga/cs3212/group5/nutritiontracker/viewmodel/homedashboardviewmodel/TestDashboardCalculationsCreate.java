package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestDashboardCalculationsCreate {
    private HomeDashboardViewModel vm;
    private PrimaryGoal placeholderGoal;
    DietGoals dietGoals;
    FoodLog currentFoodLog;
    
	private DietGoals createDietGoals(PrimaryGoal goal) {
		return new DietGoals(goal, 0, 0, 0, 0, 0, 0, Collections.emptyList());
	}

	private FoodLog createFoodLog() {
		return new FoodLog(LocalDate.of(2026, 3, 25));
	}
	
	private User createUser(DietGoals dietgoals, FoodLog foodlog) {
		User user = new User("username", "password", "name", dietgoals, foodlog);
		return user;
	}
	
    @BeforeEach
    void setup() {
		dietGoals = this.createDietGoals(PrimaryGoal.CALORIE);
		currentFoodLog = this.createFoodLog();
		User user = this.createUser(dietGoals, currentFoodLog);
		
    	this.placeholderGoal = PrimaryGoal.CALORIE;
        this.vm = new HomeDashboardViewModel(user);
    }

    @Test
    void TestCreateNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
        	HomeDashboardViewModel.create(
                null,
                this.placeholderGoal
            );
        });
    }
    
    @Test
    void TestCreateNullGoal() {
        assertThrows(IllegalArgumentException.class, () -> {
        	HomeDashboardViewModel.create(
                this.vm.getCurrentUser(),
                null
            );
        });
    }
    
    @Test
    void testCalculateConsumedAmountProtein() {
    	this.vm.setCurrentUser(this.createUser(this.createDietGoals(PrimaryGoal.PROTEIN), currentFoodLog));
    	
    }
}
