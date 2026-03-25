package edu.westga.cs3212.group5.nutritiontracker.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;

public class TestUserConstructor {

	@Test
	public void testNullUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User(null, "password", "name", this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testBlankUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("   ", "password", "name", this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testNullPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", null, "name", this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testBlankPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", "   ", "name", this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testNullName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", "password", null, this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testBlankName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", "password", "   ", this.createDietGoals(), this.createFoodLog());
		});
	}

	@Test
	public void testNullDietGoals() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", "password", "name", null, this.createFoodLog());
		});
	}

	@Test
	public void testNullCurrentFoodLog() {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("username", "password", "name", this.createDietGoals(), null);
		});
	}

	@Test
	public void testValidFullParametersConstructor() {
		DietGoals dietGoals = this.createDietGoals();
		FoodLog currentFoodLog = this.createFoodLog();
		User user = new User("username", "password", "name", dietGoals, currentFoodLog);
		assertEquals("username", user.getUsername());
		assertEquals("password", user.getPassword());
		assertEquals("name", user.getName());
		assertSame(dietGoals, user.getDietGoals());
		assertSame(currentFoodLog, user.getCurrentFoodLog());
	}

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
	}

	private FoodLog createFoodLog() {
		return new FoodLog(LocalDate.of(2026, 3, 25));
	}

}
