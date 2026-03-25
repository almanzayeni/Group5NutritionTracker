package edu.westga.cs3212.group5.nutritiontracker.server.loginrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.server.LoginRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateLoginRequest {

	@Test
	public void testNullUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			LoginRequestHandler.createLoginRequest(null, "password");
		});
	}

	@Test
	public void testBlankUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			LoginRequestHandler.createLoginRequest("   ", "password");
		});
	}

	@Test
	public void testNullPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			LoginRequestHandler.createLoginRequest("username", null);
		});
	}

	@Test
	public void testBlankPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			LoginRequestHandler.createLoginRequest("username", "   ");
		});
	}

	@Test
	public void testValidLoginRequest() throws Exception {
		String result = LoginRequestHandler.createLoginRequest("username", "password");
		JsonNode root = new ObjectMapper().readTree(result);
		assertEquals(ServerConstants.AUTHENTICATE_LOGIN_REQUEST_TYPE,
				root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("username", root.get(ServerConstants.KEY_USERNAME).asText());
		assertEquals("password", root.get(ServerConstants.KEY_PASSWORD).asText());
	}

	@Test
	public void testSerializationFailure() {
		try (MockedConstruction<ObjectMapper> mockedMapper = Mockito.mockConstruction(ObjectMapper.class, (mock, context) -> {
			when(mock.writeValueAsString(Mockito.any())).thenThrow(new RuntimeException("serialization failed"));
		})) {
			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				LoginRequestHandler.createLoginRequest("username", "password");
			});
			assertEquals("Failed to create login request", exception.getMessage());
			assertTrue(mockedMapper.constructed().size() > 0);
		}
	}

}
