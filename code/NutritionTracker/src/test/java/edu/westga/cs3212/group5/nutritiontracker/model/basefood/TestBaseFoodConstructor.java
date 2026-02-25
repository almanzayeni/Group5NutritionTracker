package edu.westga.cs3212.group5.nutritiontracker.model.basefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestBaseFoodConstructor {
	
	@Test
	public void testPortionSizeLessThan1() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 0, 1, 1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testNegativeCalories() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, -1, 1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testNegativeProtein() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, -1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testNegativeFat() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, 1, -1, 1, 1, 1);
		});
	}
	
	@Test
	public void testNegativeSugar() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, 1, 1, -1, 1, 1);
		});
	}
	
	@Test
	public void testNegativeCarbohydrates() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, 1, 1, 1, -1, 1);
		});
	}
	
	@Test
	public void testNegativeSodium() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", QuantityCategory.WEIGHT, 1, 1, 1, 1, 1, 1, -1);
		});
	}
	
	@Test
	public void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood(null, QuantityCategory.WEIGHT, 1, 1, 1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("   ", QuantityCategory.WEIGHT, 1, 1, 1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testNullQuantityCategory() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BaseFood("Strawberry", null, 1, 1, 1, 1, 1, 1, 1);
		});
	}
	
	@Test
	public void testZeroParametersConstructor() {
		BaseFood food = new BaseFood();
		assertNotNull(food);
	}
	
	@Test
	public void testValidFullParametersConstructorWholeNumbers() {
		BaseFood food = new BaseFood("Strawberry", QuantityCategory.SERVING, 1, 1, 1, 1, 1, 1, 1);
		assertEquals("Strawberry", food.getDescription());
		assertEquals(QuantityCategory.SERVING, food.getQuantityCategory());
		assertEquals(1, food.getPortionSize(), 0.001);
		assertEquals(1, food.getCalories(), 0.001);
		assertEquals(1, food.getProtein(), 0.001);
		assertEquals(1, food.getFat(), 0.001);
		assertEquals(1, food.getSugar(), 0.001);
		assertEquals(1, food.getCarbohydrates(), 0.001);
		assertEquals(1, food.getSodium(), 0.001);
	}
	
	@Test
	public void testValidFullParametersConstructorDecimalNumbers() {
		BaseFood food = new BaseFood("Apple", QuantityCategory.WEIGHT, 1.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
		assertEquals("Apple", food.getDescription());
		assertEquals(1.1, food.getPortionSize(), 0.001);
		assertEquals(0.11, food.getCalories(), 0.001);
		assertEquals(0.11, food.getProtein(), 0.001);
		assertEquals(0.11, food.getFat(), 0.001);
		assertEquals(0.11, food.getSugar(), 0.001);
		assertEquals(0.11, food.getCarbohydrates(), 0.001);
		assertEquals(0.11, food.getSodium(), 0.001);
	}
	
	@Test
	public void testValidFullParametersConstructorMixedNumbers() {
		BaseFood food = new BaseFood("Strawberry", QuantityCategory.SERVING, 2, 5000, 8.5, 1.75, 38.6, 42, 13.2);
		assertEquals("Strawberry", food.getDescription());
		assertEquals(QuantityCategory.SERVING, food.getQuantityCategory());
		assertEquals(2, food.getPortionSize(), 0.001);
		assertEquals(10000, food.getCalories(), 0.001);
		assertEquals(17, food.getProtein(), 0.001);
		assertEquals(3.5, food.getFat(), 0.001);
		assertEquals(77.2, food.getSugar(), 0.001);
		assertEquals(84, food.getCarbohydrates(), 0.001);
		assertEquals(26.4, food.getSodium(), 0.001);
	}
	
}
