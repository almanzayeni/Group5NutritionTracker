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

public class TestSelectedDate {

    private HomeDashboardViewModel vm;

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

    @BeforeEach
    void setup() {
    	FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 3, 25));
    	User user = new User("username", "password", "name", this.createDietGoals(), currentFoodLog);
        this.vm = new HomeDashboardViewModel(user);
    }
    
    @Test
    void testSetSelectedDateUpdatesValue() {
    	LocalDate date = LocalDate.of(2026, 3, 3);
    	
    	this.vm.setSelectedDate(date);
    	
    	assertEquals(date, this.vm.getSelectedDate());
    }
    
    @Test
    void testSetSelectedDateUpdatesProperty() {
    	LocalDate date = LocalDate.of(2026, 3, 3);
    	
    	this.vm.setSelectedDate(date);
    	
    	assertEquals(date, this.vm.getSelectedDateProperty().get());
    }

    @Test
    void testDateStringPropertyTracksSelectedDate() {
    	LocalDate date = LocalDate.of(2026, 3, 3);

    	this.vm.setSelectedDate(date);

    	assertEquals("3-3-2026", this.vm.getDateStringProperty().get());
    }
}
