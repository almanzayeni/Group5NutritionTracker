package edu.westga.cs3212.group5.nutritiontracker.server.createaccountrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.CreateAccountRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestHandleCreateAccountRequest {

	@Test
	public void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.handleCreateAccountRequest(null);
		});
	}

	@Test
	public void testBlankRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.handleCreateAccountRequest("   ");
		});
	}

	@Test
	public void testNullResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(null);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.handleCreateAccountRequest("request");
			});
			assertEquals("Failed to handle create account request", exception.getMessage());
			assertEquals("Received empty response from server", exception.getCause().getMessage());
		}
	}

	@Test
	public void testBlankResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn("   ");

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.handleCreateAccountRequest("request");
			});
			assertEquals("Failed to handle create account request", exception.getMessage());
			assertEquals("Received empty response from server", exception.getCause().getMessage());
		}
	}

	@Test
	public void testFailureMessageResponse() {
		String response = "{\"failure_message\":\"Username already exists\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.handleCreateAccountRequest("request");
			});
			assertEquals("Failed to handle create account request", exception.getMessage());
			assertEquals("Create Account failed: Username already exists", exception.getCause().getMessage());
		}
	}

	@Test
	public void testMissingUserResponse() {
		String response = "{\"status\":\"1\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.handleCreateAccountRequest("request");
			});
			assertEquals("Failed to handle create account request", exception.getMessage());
			assertEquals("Create Account failed: User data is null", exception.getCause().getMessage());
		}
	}

	@Test
	public void testServerClientException() throws Exception {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenThrow(new Exception("transport failed"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.handleCreateAccountRequest("request");
			});
			assertEquals("Failed to handle create account request", exception.getMessage());
			assertEquals("transport failed", exception.getCause().getMessage());
		}
	}

	@Test
	public void testSuccessfulCreateAccountResponse() throws Exception {
		User expectedUser = this.createUser();
		String response = JsonMapperFactory.create()
				.writeValueAsString(Collections.singletonMap(ServerConstants.KEY_USER, expectedUser));

		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			User result = CreateAccountRequestHandler.handleCreateAccountRequest("request");

			assertEquals(expectedUser.getUsername(), result.getUsername());
			assertEquals(expectedUser.getPassword(), result.getPassword());
			assertEquals(expectedUser.getName(), result.getName());
			assertEquals(expectedUser.getDietGoals().getPrimaryGoal(), result.getDietGoals().getPrimaryGoal());
			assertEquals(expectedUser.getCurrentFoodLog().getDate(), result.getCurrentFoodLog().getDate());
		}
	}

	private User createUser() {
		DietGoals dietGoals = new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250,
				Collections.singletonList("Stay consistent"));
		FoodLog currentFoodLog = new FoodLog(LocalDate.of(2026, 3, 25));
		return new User("username", "password", "name", dietGoals, currentFoodLog);
	}
}
