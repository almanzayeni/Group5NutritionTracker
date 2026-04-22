package edu.westga.cs3212.group5.nutritiontracker.server.getdayoffoodrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.server.GetDayOfFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestHandleRequest {

	@Test
	void testHandleRequestNullUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.handleRequest(null, LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testHandleRequestBlankUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.handleRequest("   ", LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testHandleRequestNullDateThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.handleRequest("username", null));
	}

	@Test
	void testHandleRequestNullResponseWrapsRuntimeException() {
		LocalDate date = LocalDate.of(2026, 4, 21);
		String request = GetDayOfFoodRequestHandler.createRequest("username", date);

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn(null);

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> GetDayOfFoodRequestHandler.handleRequest("username", date));
			assertEquals("Failed to handle DayOfFood request", exception.getMessage());
			assertEquals("Received empty response from server", exception.getCause().getMessage());
		}
	}

	@Test
	void testHandleRequestBlankResponseWrapsRuntimeException() {
		LocalDate date = LocalDate.of(2026, 4, 21);
		String request = GetDayOfFoodRequestHandler.createRequest("username", date);

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn("   ");

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> GetDayOfFoodRequestHandler.handleRequest("username", date));
			assertEquals("Failed to handle DayOfFood request", exception.getMessage());
			assertEquals("Received empty response from server", exception.getCause().getMessage());
		}
	}

	@Test
	void testHandleRequestFailureMessageWrapsRuntimeException() {
		LocalDate date = LocalDate.of(2026, 4, 21);
		String request = GetDayOfFoodRequestHandler.createRequest("username", date);
		String response = "{\"failure_message\":\"not found\"}";

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> GetDayOfFoodRequestHandler.handleRequest("username", date));
			assertEquals("Failed to handle DayOfFood request", exception.getMessage());
			assertEquals("DayOfFood failed: not found", exception.getCause().getMessage());
		}
	}

	@Test
	void testHandleRequestMissingFoodLogWrapsRuntimeException() {
		LocalDate date = LocalDate.of(2026, 4, 21);
		String request = GetDayOfFoodRequestHandler.createRequest("username", date);
		String response = "{\"status\":\"" + ServerConstants.SUCCESS_STATUS + "\"}";

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class,
					() -> GetDayOfFoodRequestHandler.handleRequest("username", date));
			assertEquals("Failed to handle DayOfFood request", exception.getMessage());
			assertEquals("FoodLog missing from response", exception.getCause().getMessage());
		}
	}

	@Test
	void testHandleRequestValidResponseReturnsFoodLog() throws Exception {
		LocalDate date = LocalDate.of(2026, 4, 21);
		String request = GetDayOfFoodRequestHandler.createRequest("username", date);
		FoodLog expectedLog = new FoodLog(date);
		String response = JsonMapperFactory.create()
				.writeValueAsString(java.util.Map.of(ServerConstants.KEY_FOOD_LOG, expectedLog));

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send(request)).thenReturn(response);

			FoodLog result = GetDayOfFoodRequestHandler.handleRequest("username", date);

			assertEquals(expectedLog.getDate(), result.getDate());
			assertTrue(result.getBreakfast().isEmpty());
			assertTrue(result.getLunch().isEmpty());
			assertTrue(result.getDinner().isEmpty());
			assertTrue(result.getSnacks().isEmpty());
		}
	}
}
