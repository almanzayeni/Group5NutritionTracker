package edu.westga.cs3212.group5.nutritiontracker.server.addfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestHandleAddFoodRequest {

	@Test
	public void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			AddFoodRequestHandler.handleAddFoodRequest(null);
		});
	}

	@Test
	public void testBlankRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			AddFoodRequestHandler.handleAddFoodRequest("   ");
		});
	}

	@Test
	public void testServerReturnsSuccess() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request"))
					.thenReturn("{\"status\":\"" + ServerConstants.SUCCESS_STATUS + "\"}");

			assertDoesNotThrow(() -> {
				AddFoodRequestHandler.handleAddFoodRequest("request");
			});
		}
	}

	@Test
	public void testServerReturnsFailureMessage() {
		String response = "{\"failure_message\":\"food already exists\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				AddFoodRequestHandler.handleAddFoodRequest("request");
			});
			assertEquals("Add food failed: food already exists", exception.getMessage());
		}
	}

	@Test
	public void testServerReturnsBadStatus() {
		String response = "{\"status\":\"" + ServerConstants.BAD_MESSAGE_STATUS + "\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				AddFoodRequestHandler.handleAddFoodRequest("request");
			});
			assertEquals("Add food failed with status: " + ServerConstants.BAD_MESSAGE_STATUS, exception.getMessage());
		}
	}

	@Test
	public void testServerReturnsEmptyResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn("");

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				AddFoodRequestHandler.handleAddFoodRequest("request");
			});
			assertEquals("Received empty response from server", exception.getMessage());
		}
	}

	@Test
	public void testServerThrowsException() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenThrow(new Exception("connection refused"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				AddFoodRequestHandler.handleAddFoodRequest("request");
			});
			assertEquals("Failed to handle add food request", exception.getMessage());
			assertEquals("connection refused", exception.getCause().getMessage());
		}
	}
}
