package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestRemove {
	
    HomeDashboardViewModel vm;
    BaseFood testFood;
    BaseFood testFood2;		
    
    @BeforeEach
    void setup() {
        this.vm = new HomeDashboardViewModel();
        this.testFood =
        		new BaseFood("green apple", QuantityCategory.QUANTITY, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
        this.testFood2 =
        		new BaseFood("red apple", QuantityCategory.QUANTITY, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
        
    	this.vm.addToLunch(testFood);
    	this.vm.addToLunch(testFood2);
    	
    	this.vm.addToBreakfast(testFood);
    	this.vm.addToBreakfast(testFood2);
    	
    	this.vm.addToDinner(testFood);
    	this.vm.addToDinner(testFood2);
    	
    	this.vm.addToSnacks(testFood);
    	this.vm.addToSnacks(testFood2);
    }
    
    @Test
    void TestRemoveSnacks() {
    	this.vm.removeFromSnacks(testFood);
    	this.vm.removeFromSnacks(testFood2);
    	
        assertEquals(0, this.vm.getSnacksItems().size());
    }
    
    @Test
    void TestRemoveBreakfast() {
    	this.vm.removeFromBreakfast(testFood);
    	this.vm.removeFromBreakfast(testFood2);
    	
        assertEquals(0, this.vm.getBreakfastItems().size());
    }
    
    @Test
    void TestRemoveLunch() {
    	this.vm.removeFromLunch(testFood);
    	this.vm.removeFromLunch(testFood2);
    	
        assertEquals(0, this.vm.getLunchItems().size());
    }
    
    @Test
    void TestRemoveDinner() {
    	this.vm.removeFromDinner(testFood);
    	this.vm.removeFromDinner(testFood2);
    	
        assertEquals(0, this.vm.getDinnerItems().size());
    }
}
