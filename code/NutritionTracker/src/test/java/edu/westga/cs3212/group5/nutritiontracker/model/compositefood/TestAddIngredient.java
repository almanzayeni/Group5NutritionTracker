package edu.westga.cs3212.group5.nutritiontracker.model.compositefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class TestAddIngredient {
	private BaseFood ingredient1;
	private BaseFood baseIngredientToAdd;
	private CompositeFood ingredient2;
	private CompositeFood compositeIngredientToAdd;
	private CompositeFood compositeFoodToTest;
	private List<FoodItem> startingIngredients;
	@BeforeEach
	public void setUp() {
		this.startingIngredients = new ArrayList<FoodItem>();
		this.ingredient1 = new BaseFood("ingredient1", QuantityCategory.QUANTITY, 1, 100, 1, 2, 3, 4, 5);
		this.startingIngredients.add(this.ingredient1);
		this.ingredient2 = new CompositeFood("ingredient2", QuantityCategory.WEIGHT, 1, this.startingIngredients, 100, 1, 2, 3, 4, 5);
		this.startingIngredients.add(this.ingredient2);
		this.baseIngredientToAdd = new BaseFood("baseIngredientToAdd", QuantityCategory.SERVING, 1, 200, 2, 4, 6, 8, 10);
		this.compositeIngredientToAdd = new CompositeFood("compositeIngredientToAdd", QuantityCategory.QUANTITY, 1, this.startingIngredients, 200, 2, 4, 6, 8, 10);
		this.compositeFoodToTest = new CompositeFood("compositeFoodToTest", QuantityCategory.WEIGHT, 1, this.startingIngredients, 200, 2, 4, 6, 8, 10);
	}
	
	@Test
	public void testNullIngredient() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.addIngredient(null);
		});
	}
	
	@Test
	public void testDuplicateIngredient() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.compositeFoodToTest.addIngredient(this.ingredient1);
		});
	}
	
	@Test
	public void testAddBaseIngredient() {
		this.compositeFoodToTest.addIngredient(baseIngredientToAdd);
		assertTrue(this.compositeFoodToTest.getIngredients().contains(this.baseIngredientToAdd));
		assertEquals(400, this.compositeFoodToTest.getCalories());
		assertEquals(4, this.compositeFoodToTest.getProtein());
		assertEquals(8, this.compositeFoodToTest.getFat());
		assertEquals(12, this.compositeFoodToTest.getSugar());
		assertEquals(16, this.compositeFoodToTest.getCarbohydrates());
		assertEquals(20, this.compositeFoodToTest.getSodium());
	}
	
	@Test
	public void testAddCompositeIngredient() {
		this.compositeFoodToTest.addIngredient(compositeIngredientToAdd);
		assertTrue(this.compositeFoodToTest.getIngredients().contains(this.compositeIngredientToAdd));
		assertEquals(400, this.compositeFoodToTest.getCalories());
		assertEquals(4, this.compositeFoodToTest.getProtein());
		assertEquals(8, this.compositeFoodToTest.getFat());
		assertEquals(12, this.compositeFoodToTest.getSugar());
		assertEquals(16, this.compositeFoodToTest.getCarbohydrates());
		assertEquals(20, this.compositeFoodToTest.getSodium());
	}
}
