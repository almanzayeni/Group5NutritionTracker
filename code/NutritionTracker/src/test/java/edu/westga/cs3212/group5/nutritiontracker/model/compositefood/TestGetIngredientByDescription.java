package edu.westga.cs3212.group5.nutritiontracker.model.compositefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestGetIngredientByDescription {
	private BaseFood ingredient1;
	private CompositeFood ingredient2;
	private CompositeFood compositeFoodToTest;
	private List<FoodItem> startingIngredients;
	@BeforeEach
	public void setUp() {
		this.startingIngredients = new ArrayList<FoodItem>();
		this.ingredient1 = new BaseFood("ingredient1", QuantityCategory.QUANTITY, 1, 100, 1, 2, 3, 4, 5);
		this.startingIngredients.add(this.ingredient1);
		this.ingredient2 = new CompositeFood("ingredient2", QuantityCategory.WEIGHT, 1, this.startingIngredients, 100, 1, 2, 3, 4, 5);
		this.startingIngredients.add(this.ingredient2);
		this.compositeFoodToTest = new CompositeFood("compositeFoodToTest", QuantityCategory.WEIGHT, 1, this.startingIngredients, 200, 2, 4, 6, 8, 10);
	}
	
	@Test
	public void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.getIngredientByDescription(null);
		});
	}
	
	@Test
	public void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.getIngredientByDescription("   ");
		});
	}
	
	@Test
	public void testIngredientNotFound() {
		FoodItem result = this.compositeFoodToTest.getIngredientByDescription("nonexistent ingredient");
		assertNull(result);
	}
	
	@Test
	public void testBaseIngredientFound() {
		FoodItem result = this.compositeFoodToTest.getIngredientByDescription("ingredient1");
		assertEquals(this.ingredient1, result);
	}
	
	@Test
	public void testCompositeIngredientFound() {
		FoodItem result = this.compositeFoodToTest.getIngredientByDescription("ingredient2");
		assertEquals(this.ingredient2, result);
	}
	
}
