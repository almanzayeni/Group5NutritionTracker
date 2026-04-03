package edu.westga.cs3212.group5.nutritiontracker.server.createaccountrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.server.CreateAccountRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateCreateAccountRequest {

	@Test
	public void testNullUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.createCreateAccountRequest(null, "password", "name", this.createDietGoals());
		});
	}

	@Test
	public void testBlankUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.createCreateAccountRequest("   ", "password", "name", this.createDietGoals());
		});
	}

	@Test
	public void testNullPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.createCreateAccountRequest("username", null, "name", this.createDietGoals());
		});
	}

	@Test
	public void testBlankPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.createCreateAccountRequest("username", "   ", "name", this.createDietGoals());
		});
	}

	@Test
	public void testNullDietGoals() {
		assertThrows(IllegalArgumentException.class, () -> {
			CreateAccountRequestHandler.createCreateAccountRequest("username", "password", "name", null);
		});
	}

	@Test
	public void testValidCreateAccountRequest() throws Exception {
		String result = CreateAccountRequestHandler.createCreateAccountRequest("username", "password", "name",
				this.createDietGoals());

		JsonNode root = JsonMapperFactory.create().readTree(result);
		JsonNode userNode = root.get(ServerConstants.KEY_USER);
		JsonNode dietGoalsNode = userNode.get(ServerConstants.KEY_DIET_GOALS);
		JsonNode currentFoodLogNode = userNode.get("currentFoodLog");

		assertEquals(ServerConstants.CREATE_ACCOUNT_REQUEST_TYPE, root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("username", userNode.get(ServerConstants.KEY_USERNAME).asText());
		assertEquals("password", userNode.get(ServerConstants.KEY_PASSWORD).asText());
		assertEquals("name", userNode.get(ServerConstants.KEY_NAME).asText());
		assertEquals(PrimaryGoal.CALORIE.name(), dietGoalsNode.get(ServerConstants.KEY_PRIMARY_GOAL).asText());
		assertTrue(currentFoodLogNode.has("date"));
		assertTrue(currentFoodLogNode.has("breakfast"));
		assertTrue(currentFoodLogNode.has("lunch"));
		assertTrue(currentFoodLogNode.has("dinner"));
		assertTrue(currentFoodLogNode.has("snacks"));
	}

	@Test
	public void testSerializationFailure() throws JsonProcessingException {
		ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
		when(mockMapper.writeValueAsString(Mockito.any()))
				.thenThrow(new JsonProcessingException("serialization failed") {
				});

		try (MockedStatic<JsonMapperFactory> mockedFactory = Mockito.mockStatic(JsonMapperFactory.class)) {
			mockedFactory.when(JsonMapperFactory::create).thenReturn(mockMapper);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				CreateAccountRequestHandler.createCreateAccountRequest("username", "password", "name",
						this.createDietGoals());
			});
			assertEquals("Failed to create create account request", exception.getMessage());
		}
	}

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.CALORIE, 2000, 100, 60, 50, 2300, 250,
				Collections.singletonList("Stay consistent"));
	}
}
