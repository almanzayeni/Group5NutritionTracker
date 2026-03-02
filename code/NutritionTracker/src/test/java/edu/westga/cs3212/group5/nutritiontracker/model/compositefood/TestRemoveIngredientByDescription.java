package edu.westga.cs3212.group5.nutritiontracker.model.compositefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestRemoveIngredientByDescription {
	private BaseFood ingredient1;
	private CompositeFood ingredient2;
	private CompositeFood compositeFoodToTest;
	private List<FoodItem> startingIngredients;
	@BeforeEach
	public void setUp() {
		this.startingIngredients = new ArrayList<FoodItem>();
		this.ingredient1 = new BaseFood("ingredient1", QuantityCategory.QUANTITY, 1, 100, 1, 2, 3, 4, 5);
		this.startingIngredients.add(this.ingredient1);
		this.ingredient2 = new CompositeFood("ingredient2", QuantityCategory.WEIGHT, 1, this.startingIngredients);
		this.startingIngredients.add(this.ingredient2);
		this.compositeFoodToTest = new CompositeFood("compositeFoodToTest", QuantityCategory.WEIGHT, 1, this.startingIngredients);
	}
	
	@Test
	public void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.removeIngredientByDescription(null);
		});
	}
	
	@Test
	public void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.removeIngredientByDescription("   ");
		});
	}
	
	@Test
	public void testIngredientNotFound() {
		assertFalse(this.compositeFoodToTest.removeIngredientByDescription("nonexistent ingredient"));
	}
	
	@Test
	public void testRemoveBaseIngredient() {
		assertTrue(this.compositeFoodToTest.removeIngredientByDescription("ingredient1"));
		assertFalse(this.compositeFoodToTest.getIngredientsList().contains(this.ingredient1));
		assertEquals(100, this.compositeFoodToTest.getCalories());
		assertEquals(1, this.compositeFoodToTest.getProtein());
		assertEquals(2, this.compositeFoodToTest.getFat());
		assertEquals(3, this.compositeFoodToTest.getSugar());
		assertEquals(4, this.compositeFoodToTest.getCarbohydrates());
		assertEquals(5, this.compositeFoodToTest.getSodium());
	}
	
	@Test
	public void testRemoveCompositeIngredient() {
		assertTrue(this.compositeFoodToTest.removeIngredientByDescription("ingredient2"));
		assertFalse(this.compositeFoodToTest.getIngredientsList().contains(this.ingredient2));
		assertEquals(100, this.compositeFoodToTest.getCalories());
		assertEquals(1, this.compositeFoodToTest.getProtein());
		assertEquals(2, this.compositeFoodToTest.getFat());
		assertEquals(3, this.compositeFoodToTest.getSugar());
		assertEquals(4, this.compositeFoodToTest.getCarbohydrates());
		assertEquals(5, this.compositeFoodToTest.getSodium());
	}
	
}
