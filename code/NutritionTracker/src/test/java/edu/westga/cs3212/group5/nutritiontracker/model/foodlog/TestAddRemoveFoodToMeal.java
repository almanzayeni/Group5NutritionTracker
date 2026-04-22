package edu.westga.cs3212.group5.nutritiontracker.model.foodlog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.MealType;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestAddRemoveFoodToMeal {

	private FoodLog log;
	private FoodItem food;

	@BeforeEach
	void setUp() {
		this.log = new FoodLog(LocalDate.of(2026, 4, 21));
		this.food = new BaseFood("Toast", QuantityCategory.SERVING, 1, 120, 4, 2, 2, 180, 20);
	}

	@Test
	void testAddFoodToMealNullMealTypeThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> this.log.addFoodToMeal(null, this.food));
	}

	@Test
	void testAddFoodToMealNullFoodThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> this.log.addFoodToMeal(MealType.BREAKFAST, null));
	}

	@Test
	void testAddFoodToBreakfastAddsItem() {
		this.log.addFoodToMeal(MealType.BREAKFAST, this.food);

		assertEquals(1, this.log.getBreakfast().size());
		assertTrue(this.log.getBreakfast().contains(this.food));
	}

	@Test
	void testAddFoodToLunchAddsItem() {
		this.log.addFoodToMeal(MealType.LUNCH, this.food);

		assertEquals(1, this.log.getLunch().size());
		assertTrue(this.log.getLunch().contains(this.food));
	}

	@Test
	void testAddFoodToDinnerAddsItem() {
		this.log.addFoodToMeal(MealType.DINNER, this.food);

		assertEquals(1, this.log.getDinner().size());
		assertTrue(this.log.getDinner().contains(this.food));
	}

	@Test
	void testAddFoodToSnacksAddsItem() {
		this.log.addFoodToMeal(MealType.SNACKS, this.food);

		assertEquals(1, this.log.getSnacks().size());
		assertTrue(this.log.getSnacks().contains(this.food));
	}

	@Test
	void testRemoveFoodFromMealNullMealTypeThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> this.log.removeFoodFromMeal(null, this.food));
	}

	@Test
	void testRemoveFoodFromMealNullFoodThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> this.log.removeFoodFromMeal(MealType.BREAKFAST, null));
	}

	@Test
	void testRemoveFoodFromBreakfastRemovesItem() {
		this.log.addFoodToMeal(MealType.BREAKFAST, this.food);

		this.log.removeFoodFromMeal(MealType.BREAKFAST, this.food);

		assertTrue(this.log.getBreakfast().isEmpty());
	}

	@Test
	void testRemoveFoodFromLunchRemovesItem() {
		this.log.addFoodToMeal(MealType.LUNCH, this.food);

		this.log.removeFoodFromMeal(MealType.LUNCH, this.food);

		assertTrue(this.log.getLunch().isEmpty());
	}

	@Test
	void testRemoveFoodFromDinnerRemovesItem() {
		this.log.addFoodToMeal(MealType.DINNER, this.food);

		this.log.removeFoodFromMeal(MealType.DINNER, this.food);

		assertTrue(this.log.getDinner().isEmpty());
	}

	@Test
	void testRemoveFoodFromSnacksRemovesItem() {
		this.log.addFoodToMeal(MealType.SNACKS, this.food);

		this.log.removeFoodFromMeal(MealType.SNACKS, this.food);

		assertTrue(this.log.getSnacks().isEmpty());
	}
}
