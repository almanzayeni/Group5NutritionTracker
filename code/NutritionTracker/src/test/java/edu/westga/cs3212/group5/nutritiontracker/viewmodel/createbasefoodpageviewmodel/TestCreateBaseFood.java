package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createbasefoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateBaseFoodPageViewModel;

public class TestCreateBaseFood {
	private static final Path TARGET = Path.of("test.json");

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(TARGET)) {
            Files.delete(TARGET);
        }
    }

    @Test
    void createBaseFoodShouldThrowIOException() {
    	if (Files.exists(TARGET)) {
            try {
            	Files.delete(TARGET);
            } catch (IOException e) {
				throw new RuntimeException("Failed to delete existing test file: " + e.getMessage(), e);
			}
        }
        try {
        	Files.createDirectory(TARGET);
        } catch (IOException e) {
        	throw new RuntimeException("Failed to create directory for test: " + e.getMessage(), e);
        }
        
        CreateBaseFoodPageViewModel vm = new CreateBaseFoodPageViewModel("test.json");
        vm.getNameProperty().set("Strawberry");
        vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.WEIGHT);
        vm.getCaloriesProperty().set(10);
        vm.getProteinProperty().set(1);
        vm.getFatProperty().set(1);
        vm.getSugarProperty().set(1);
        vm.getCarbohydratesProperty().set(1);
        vm.getSodiumProperty().set(1);

        assertThrows(IOException.class, () -> {
			vm.createBaseFood();
		});
    }
    
    @Test
    void createBaseFoodShouldThrowJsonProcessingException()
        throws JsonProcessingException {

        ObjectMapper mockMapper = mock(ObjectMapper.class);

        when(mockMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("Serialization failed") {});

        CreateBaseFoodPageViewModel vm =
                new CreateBaseFoodPageViewModel(mockMapper);

        vm.getNameProperty().set("UniqueFood");
        vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.WEIGHT);
        vm.getCaloriesProperty().set(10);
        vm.getProteinProperty().set(1);
        vm.getFatProperty().set(1);
        vm.getSugarProperty().set(1);
        vm.getCarbohydratesProperty().set(1);
        vm.getSodiumProperty().set(1);

        assertThrows(JsonProcessingException.class, () -> {
			vm.createBaseFood();
		});
    }
    
    @Test
    void createValidBaseFood() {
    	CreateBaseFoodPageViewModel vm = new CreateBaseFoodPageViewModel("test.json");
		vm.getNameProperty().set("Strawberry");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.WEIGHT);
		vm.getCaloriesProperty().set(10);
		vm.getProteinProperty().set(1);
		vm.getFatProperty().set(1);
		vm.getSugarProperty().set(1);
		vm.getCarbohydratesProperty().set(1);
		vm.getSodiumProperty().set(1);

		try {
			vm.createBaseFood();
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
		}
		
		BaseFood createdFood = null;
		try {
			String jsonString = Files.readString(TARGET);
			ObjectMapper objectMapper = new ObjectMapper();
			createdFood = objectMapper.readValue(jsonString, BaseFood.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read created food: " + e.getMessage(), e);
		}
		
		assertNotNull(createdFood);
		assertEquals("Strawberry", createdFood.getDescription());
		assertEquals(QuantityCategory.WEIGHT, createdFood.getQuantityCategory());
		assertEquals(10, createdFood.getCalories());
		assertEquals(1, createdFood.getProtein());
		assertEquals(1, createdFood.getFat());
		assertEquals(1, createdFood.getSugar());
		assertEquals(1, createdFood.getCarbohydrates());
		assertEquals(1, createdFood.getSodium());
    }
    
    @Test
    void createDuplicateBaseFoodShouldThrowIllegalArgumentException() {
		CreateBaseFoodPageViewModel vm = new CreateBaseFoodPageViewModel("test.json");
		vm.getNameProperty().set("Strawberry");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.WEIGHT);
		vm.getCaloriesProperty().set(10);
		vm.getProteinProperty().set(1);
		vm.getFatProperty().set(1);
		vm.getSugarProperty().set(1);
		vm.getCarbohydratesProperty().set(1);
		vm.getSodiumProperty().set(1);
		
		try {
			vm.createBaseFood();
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
		}
		
		assertThrows(IllegalArgumentException.class, () -> {
			vm.createBaseFood();
		});
    }
}
