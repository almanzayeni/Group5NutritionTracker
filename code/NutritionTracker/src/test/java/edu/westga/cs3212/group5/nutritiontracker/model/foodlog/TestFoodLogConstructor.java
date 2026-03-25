package edu.westga.cs3212.group5.nutritiontracker.model.foodlog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;

public class TestFoodLogConstructor {

	@Test
	public void testZeroParametersConstructor() {
		FoodLog log = new FoodLog();
		assertNotNull(log);
		assertEquals(LocalDate.now(), log.getDate());
		assertNotNull(log.getBreakfast());
		assertNotNull(log.getLunch());
		assertNotNull(log.getDinner());
		assertNotNull(log.getSnacks());
		assertEquals(0, log.getBreakfast().size());
		assertEquals(0, log.getLunch().size());
		assertEquals(0, log.getDinner().size());
		assertEquals(0, log.getSnacks().size());
	}

	@Test
	public void testValidDateOnlyConstructor() {
		LocalDate date = LocalDate.of(2026, 3, 25);
		FoodLog log = new FoodLog(date);
		assertEquals(date, log.getDate());
		assertNotNull(log.getBreakfast());
		assertNotNull(log.getLunch());
		assertNotNull(log.getDinner());
		assertNotNull(log.getSnacks());
		assertEquals(0, log.getBreakfast().size());
		assertEquals(0, log.getLunch().size());
		assertEquals(0, log.getDinner().size());
		assertEquals(0, log.getSnacks().size());
	}

	@Test
	public void testValidFullParametersConstructor() {
		LocalDate date = LocalDate.of(2025, 12, 31);
		List<FoodItem> breakfast = new ArrayList<FoodItem>();
		List<FoodItem> lunch = new ArrayList<FoodItem>();
		List<FoodItem> dinner = new ArrayList<FoodItem>();
		List<FoodItem> snacks = new ArrayList<FoodItem>();
		FoodLog log = new FoodLog(date, breakfast, lunch, dinner, snacks);
		assertEquals(date, log.getDate());
		assertSame(breakfast, log.getBreakfast());
		assertSame(lunch, log.getLunch());
		assertSame(dinner, log.getDinner());
		assertSame(snacks, log.getSnacks());
	}

}
