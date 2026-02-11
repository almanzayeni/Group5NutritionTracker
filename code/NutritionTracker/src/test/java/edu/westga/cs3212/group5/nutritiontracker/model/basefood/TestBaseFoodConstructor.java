package edu.westga.cs3212.group5.nutritiontracker.model.basefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestBaseFoodConstructor {
	@Test
	public void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood(null, QuantityCategory.WEIGHT, 100.0, 52.0);
		});
	}
	
	@Test
	public void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("   ", QuantityCategory.WEIGHT, 100.0, 52.0);
		});
	}
	
	@Test
	public void testNullQuantityCategory() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Apple", null, 100.0, 52.0);
		});
	}
	
	@Test
	public void testNegativeQuantityValue() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Apple", QuantityCategory.WEIGHT, -1.0, 52.0);
		});
	}
	
	@Test
	public void testNegativeCalories() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Apple", QuantityCategory.WEIGHT, 100.0, -1.0);
		});
	}
	
	@Test
	public void testZeroQuantityValue() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Apple", QuantityCategory.WEIGHT, 0.0, 52.0);
		});
	}
	
	@Test
	public void testZeroCalories() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Apple", QuantityCategory.WEIGHT, 100.0, 0.0);
		});
	}
	
	@Test
	public void testValidMinimumParametersConstructorWholeNumbers() {
		BaseFood food = new BaseFood("Apple", QuantityCategory.WEIGHT, 1, 1);
		assertEquals("Apple", food.getDescription());
		assertEquals(1, food.getQuantityValue(), 0.001);
		assertEquals(1, food.getCalories(), 0.001);
		assertEquals(0, food.getProtein(), 0.001);
		assertEquals(0, food.getFat(), 0.001);
		assertEquals(0, food.getSugar(), 0.001);
		assertEquals(0, food.getCarbohydrates(), 0.001);
		assertEquals(0, food.getSodium(), 0.001);
	}
	
	@Test
	public void testValidMinimumParametersConstructorDecimalNumbers() {
		BaseFood food = new BaseFood("Strawberry", QuantityCategory.WEIGHT, 0.1, 0.1);
		assertEquals("Strawberry", food.getDescription());
		assertEquals(0.1, food.getQuantityValue(), 0.001);
		assertEquals(0.1, food.getCalories(), 0.001);
		assertEquals(0, food.getProtein(), 0.001);
		assertEquals(0, food.getFat(), 0.001);
		assertEquals(0, food.getSugar(), 0.001);
		assertEquals(0, food.getCarbohydrates(), 0.001);
		assertEquals(0, food.getSodium(), 0.001);
	}
	
	@Test
	public void testValidFullParametersConstructorWholeNumbers() {
		BaseFood food = new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, 1, 1, 1, 1, 1);
		assertEquals("Strawberry", food.getDescription());
		assertEquals(1, food.getQuantityValue(), 0.001);
		assertEquals(1, food.getCalories(), 0.001);
		assertEquals(1, food.getProtein(), 0.001);
		assertEquals(1, food.getFat(), 0.001);
		assertEquals(1, food.getSugar(), 0.001);
		assertEquals(1, food.getCarbohydrates(), 0.001);
		assertEquals(1, food.getSodium(), 0.001);
	}
	
	@Test
	public void testValidFullParametersConstructorDecimalNumbers() {
		BaseFood food = new BaseFood("Apple", QuantityCategory.WEIGHT, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
		assertEquals("Apple", food.getDescription());
		assertEquals(0.1, food.getQuantityValue(), 0.001);
		assertEquals(0.1, food.getCalories(), 0.001);
		assertEquals(0.1, food.getProtein(), 0.001);
		assertEquals(0.1, food.getFat(), 0.001);
		assertEquals(0.1, food.getSugar(), 0.001);
		assertEquals(0.1, food.getCarbohydrates(), 0.001);
		assertEquals(0.1, food.getSodium(), 0.001);
	}
	
}
