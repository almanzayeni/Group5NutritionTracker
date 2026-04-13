package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestRemove {

    private HomeDashboardViewModel vm;
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

    	List<FoodItem> breakfast = List.of(this.breakfastFood);
    	List<FoodItem> lunch = List.of(this.lunchFood);
    	List<FoodItem> dinner = List.of(this.dinnerFood);
    	List<FoodItem> snacks = List.of(this.snackFood);
    	FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 3, 25), breakfast, lunch, dinner, snacks);
    	User user = new User("username", "password", "name", this.createDietGoals(), currentFoodLog);

        this.vm = new HomeDashboardViewModel(user);
    }
    
    @Test
    void testRemoveFromSnacks() {
    	this.vm.removeFromSnacks(this.snackFood);
    	
        assertEquals(0, this.vm.getSnacksItems().size());
        assertEquals(600.0, this.vm.totalCaloriesProperty().get(), 0.001);
    }
    
    @Test
    void testRemoveFromBreakfast() {
    	this.vm.removeFromBreakfast(this.breakfastFood);
    	
        assertEquals(0, this.vm.getBreakfastItems().size());
        assertEquals(550.0, this.vm.totalCaloriesProperty().get(), 0.001);
    }
    
    @Test
    void testRemoveFromLunch() {
    	this.vm.removeFromLunch(this.lunchFood);
    	
        assertEquals(0, this.vm.getLunchItems().size());
        assertEquals(450.0, this.vm.totalCaloriesProperty().get(), 0.001);
    }
    
    @Test
    void testRemoveFromDinner() {
    	this.vm.removeFromDinner(this.dinnerFood);
    	
        assertEquals(0, this.vm.getDinnerItems().size());
        assertEquals(350.0, this.vm.totalCaloriesProperty().get(), 0.001);
    }
}
