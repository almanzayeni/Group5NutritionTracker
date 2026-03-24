package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestAdd {
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
    }
    
    @Test
    void TestAddLunch() {
    	this.vm.addToLunch(testFood);
    	this.vm.addToLunch(testFood2);
    	
        assertEquals(2, this.vm.getLunchItems().size());
        assertEquals(testFood, this.vm.getLunchItems().get(0));
        assertEquals(testFood2, this.vm.getLunchItems().get(1));
    }
    
    @Test
    void TestAddBreakfast() {
    	this.vm.addToBreakfast(testFood);
    	this.vm.addToBreakfast(testFood2);
    	
        assertEquals(2, this.vm.getBreakfastItems().size());
        assertEquals(testFood, this.vm.getBreakfastItems().get(0));
        assertEquals(testFood2, this.vm.getBreakfastItems().get(1));
    }
    
    @Test
    void TestAddDinner() {
    	this.vm.addToDinner(testFood);
    	this.vm.addToDinner(testFood2);
    	
        assertEquals(2, this.vm.getDinnerItems().size());
        assertEquals(testFood, this.vm.getDinnerItems().get(0));
        assertEquals(testFood2, this.vm.getDinnerItems().get(1));
    }
    
    @Test
    void TestAddSnacks() {
    	this.vm.addToSnacks(testFood);
    	this.vm.addToSnacks(testFood2);
    	
        assertEquals(2, this.vm.getSnacksItems().size());
        assertEquals(testFood, this.vm.getSnacksItems().get(0));
        assertEquals(testFood2, this.vm.getSnacksItems().get(1));
    }
}
