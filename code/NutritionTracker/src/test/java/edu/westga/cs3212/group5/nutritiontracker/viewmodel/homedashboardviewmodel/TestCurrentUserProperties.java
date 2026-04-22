package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestCurrentUserProperties {

    private HomeDashboardViewModel vm;
    private User initialUser;

	private DietGoals createDietGoals(double calories) {
		return new DietGoals(PrimaryGoal.CALORIE, calories, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

    @BeforeEach
    void setup() {
    	FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 3, 25));
    	this.initialUser = new User("username", "password", "name", this.createDietGoals(2000), currentFoodLog);
        this.vm = new HomeDashboardViewModel(this.initialUser);
    }

    @Test
    void testGetCurrentUserReturnsConstructorUser() {
    	assertSame(this.initialUser, this.vm.getCurrentUser());
    }

    @Test
    void testSetCurrentUserUpdatesCurrentUser() {
    	User replacementUser = new User(
    			"otheruser",
    			"secret",
    			"Other Name",
    			this.createDietGoals(1800),
    			new FoodLog(LocalDate.of(2026, 3, 26)));

    	this.vm.setCurrentUser(replacementUser);

    	assertSame(replacementUser, this.vm.getCurrentUser());
    }

    @Test
    void testUserPropertiesReflectConstructorValues() {
    	assertEquals("name", this.vm.getUsersNameProperty().get());
    	assertSame(this.initialUser.getCurrentFoodLog(), this.vm.getCurrentUser().getCurrentFoodLog());
    	assertSame(this.initialUser.getDietGoals(), this.vm.userDietGoalsProperty().get());
    }
    
    @Test
    void testSelectedGoalProperty() {
    	assertEquals(this.vm.getCurrentUser().getDietGoals().getPrimaryGoal(), this.vm.selectedGoalProperty().get());
    }
}
