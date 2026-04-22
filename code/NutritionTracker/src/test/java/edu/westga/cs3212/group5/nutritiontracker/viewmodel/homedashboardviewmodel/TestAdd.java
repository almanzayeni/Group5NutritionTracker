package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestAdd {
    private HomeDashboardViewModel vm;
    private BaseFood testFood;
    private BaseFood testFood2;		
    
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
        this.testFood =
        		new BaseFood("green apple", QuantityCategory.QUANTITY, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
        this.testFood2 =
        		new BaseFood("red apple", QuantityCategory.QUANTITY, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
    }

    @Test
    void TestAddLunch() {
    	this.vm.addToLunch(testFood);
    	this.vm.addToLunch(testFood2);
    	
        assertEquals(2, this.vm.getCurrentUser().getCurrentFoodLog().getLunch().size());
        assertEquals(testFood, this.vm.getCurrentUser().getCurrentFoodLog().getLunch().get(0));
        assertEquals(testFood2, this.vm.getCurrentUser().getCurrentFoodLog().getLunch().get(1));
    }
    
    @Test
    void TestAddBreakfast() {
    	this.vm.addToBreakfast(testFood);
    	this.vm.addToBreakfast(testFood2);
    	
        assertEquals(2, this.vm.getCurrentUser().getCurrentFoodLog().getBreakfast().size());
        assertEquals(testFood, this.vm.getCurrentUser().getCurrentFoodLog().getBreakfast().get(0));
        assertEquals(testFood2, this.vm.getCurrentUser().getCurrentFoodLog().getBreakfast().get(1));
    }
    
    @Test
    void TestAddDinner() {
    	this.vm.addToDinner(testFood);
    	this.vm.addToDinner(testFood2);
    	
        assertEquals(2, this.vm.getCurrentUser().getCurrentFoodLog().getDinner().size());
        assertEquals(testFood, this.vm.getCurrentUser().getCurrentFoodLog().getDinner().get(0));
        assertEquals(testFood2, this.vm.getCurrentUser().getCurrentFoodLog().getDinner().get(1));
    }
    
    @Test
    void TestAddSnacks() {
    	this.vm.addToSnacks(testFood);
    	this.vm.addToSnacks(testFood2);
    	
        assertEquals(2, this.vm.getCurrentUser().getCurrentFoodLog().getSnacks().size());
        assertEquals(testFood, this.vm.getCurrentUser().getCurrentFoodLog().getSnacks().get(0));
        assertEquals(testFood2, this.vm.getCurrentUser().getCurrentFoodLog().getSnacks().get(1));
    }
}
