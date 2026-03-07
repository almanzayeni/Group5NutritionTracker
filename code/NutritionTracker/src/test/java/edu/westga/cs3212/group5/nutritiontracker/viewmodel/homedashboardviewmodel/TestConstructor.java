package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestConstructor {
    HomeDashboardViewModel vm;

    @BeforeEach
    void setup() {
        this.vm = new HomeDashboardViewModel();
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
