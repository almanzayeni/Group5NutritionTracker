package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestSelectedDate {
    HomeDashboardViewModel vm;
    BaseFood testFood;
    BaseFood testFood2;		
    
    @BeforeEach
    void setup() {
        this.vm = new HomeDashboardViewModel();
    }
    
    @Test
    void TestSetSelectedDate() {
    	LocalDate date = LocalDate.of(2026, 3, 3);
    	
    	this.vm.setSelectedDate(date);
    	
    	assertEquals(date, this.vm.getSelectedDate());
    }
    
    @Test
    void TestSetSelectedProperty() {
    	LocalDate date = LocalDate.of(2026, 3, 3);
    	
    	this.vm.setSelectedDate(date);
    	
    	assertEquals(date, this.vm.getSelectedDateProperty().get());
    }
}
