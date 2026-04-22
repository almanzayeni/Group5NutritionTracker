package edu.westga.cs3212.group5.nutritiontracker.viewmodel.homedashboardviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.GetDayOfFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

public class TestHandleDateChange {

	private HomeDashboardViewModel viewModel;
	private User user;

	@BeforeEach
	void setUp() {
		DietGoals goals = new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250, Collections.emptyList());
		FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 4, 20));
		this.user = new User("username", "password", "name", goals, currentFoodLog);
		this.viewModel = new HomeDashboardViewModel(this.user);
	}

	@Test
	void testHandleDateChangeUpdatesCurrentFoodLogAndMealLists() throws Exception {
		LocalDate requestedDate = LocalDate.of(2026, 4, 21);
		FoodLog replacementLog = new FoodLog(requestedDate);
		BaseFood breakfastFood = new BaseFood("Eggs", QuantityCategory.SERVING, 1, 140, 12, 10, 1, 140, 1);
		replacementLog.getBreakfast().add(breakfastFood);
		this.viewModel.setSelectedDate(requestedDate);
		String request = GetDayOfFoodRequestHandler.createRequest(this.user.getUsername(), requestedDate);

		try (MockedStatic<ServerClient> serverClientMock = mockStatic(ServerClient.class)) {
			String response = JsonMapperFactory.create()
					.writeValueAsString(Map.of(ServerConstants.KEY_FOOD_LOG, replacementLog));
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn(response);

			this.viewModel.handleDateChange();

			assertEquals(1, this.viewModel.getBreakfastItems().size());
			assertEquals(replacementLog.getDate(), this.viewModel.getCurrentUser().getCurrentFoodLog().getDate());
			assertEquals(breakfastFood.getDescription(), this.viewModel.getBreakfastItems().get(0).getDescription());
		}
	}

	@Test
	void testHandleDateChangeRethrowsWrappedServerException() {
		LocalDate requestedDate = LocalDate.of(2026, 4, 22);
		this.viewModel.setSelectedDate(requestedDate);
		String request = GetDayOfFoodRequestHandler.createRequest(this.user.getUsername(), requestedDate);

		try (MockedStatic<ServerClient> serverClientMock = mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenThrow(new Exception("server unavailable"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> this.viewModel.handleDateChange());
			assertEquals("Failed to handle DayOfFood request", exception.getMessage());
			assertEquals("server unavailable", exception.getCause().getMessage());
		}
	}
}
