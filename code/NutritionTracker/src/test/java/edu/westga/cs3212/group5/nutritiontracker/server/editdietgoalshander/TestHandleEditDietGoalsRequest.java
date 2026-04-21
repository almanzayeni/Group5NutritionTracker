package edu.westga.cs3212.group5.nutritiontracker.server.editdietgoalshander;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.westga.cs3212.group5.nutritiontracker.server.EditDietGoalsHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestHandleEditDietGoalsRequest {

	@Test
	public void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			EditDietGoalsHandler.handleEditDietGoalsRequest(null);
		});
	}

	@Test
	public void testBlankRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			EditDietGoalsHandler.handleEditDietGoalsRequest("   ");
		});
	}

	@Test
	public void testNullResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(null);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("Received empty response from server", exception.getMessage());
		}
	}

	@Test
	public void testBlankResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn("   ");

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("Received empty response from server", exception.getMessage());
		}
	}

	@Test
	public void testServerReturnsFailureMessage() {
		String response = "{\"failure_message\":\"diet goals could not be updated\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("Add food failed: diet goals could not be updated", exception.getMessage());
		}
	}

	@Test
	public void testServerReturnsBadStatus() {
		String response = "{\"status\":\"" + ServerConstants.BAD_MESSAGE_STATUS + "\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("Add food failed with status: " + ServerConstants.BAD_MESSAGE_STATUS, exception.getMessage());
		}
	}

	@Test
	public void testServerThrowsRuntimeException() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request"))
					.thenThrow(new RuntimeException("transport already failed"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("transport already failed", exception.getMessage());
		}
	}

	@Test
	public void testServerThrowsCheckedException() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenThrow(new Exception("connection refused"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
			assertEquals("Failed to handle add food request", exception.getMessage());
			assertEquals("connection refused", exception.getCause().getMessage());
		}
	}

	@Test
	public void testServerReturnsSuccess() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request"))
					.thenReturn("{\"status\":\"" + ServerConstants.SUCCESS_STATUS + "\"}");

			assertDoesNotThrow(() -> {
				EditDietGoalsHandler.handleEditDietGoalsRequest("request");
			});
		}
	}
}
