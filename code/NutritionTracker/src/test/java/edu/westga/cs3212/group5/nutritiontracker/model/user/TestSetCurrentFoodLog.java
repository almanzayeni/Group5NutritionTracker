package edu.westga.cs3212.group5.nutritiontracker.model.user;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;

public class TestSetCurrentFoodLog {

	private User user;

	@BeforeEach
	void setUp() {
		DietGoals goals = new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
		this.user = new User("username", "password", "name", goals, new FoodLog(LocalDate.of(2026, 4, 20)));
	}

	@Test
	void testSetCurrentFoodLogNullThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> this.user.setCurrentFoodLog(null));
	}

	@Test
	void testSetCurrentFoodLogValidLogUpdatesCurrentFoodLog() {
		FoodLog replacementLog = new FoodLog(LocalDate.of(2026, 4, 21));

		this.user.setCurrentFoodLog(replacementLog);

		assertSame(replacementLog, this.user.getCurrentFoodLog());
	}
}
