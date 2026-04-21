package edu.westga.cs3212.group5.nutritiontracker.server.editdietgoalshander;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.server.EditDietGoalsHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateEditDietGoalsRequest {

	@Test
	public void testConstructor() {
		assertDoesNotThrow(() -> {
			new EditDietGoalsHandler();
		});
	}

	@Test
	public void testNullUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			EditDietGoalsHandler.createEditDietGoalsRequest(null, this.createDietGoals());
		});
	}

	@Test
	public void testBlankUsername() {
		assertThrows(IllegalArgumentException.class, () -> {
			EditDietGoalsHandler.createEditDietGoalsRequest("   ", this.createDietGoals());
		});
	}

	@Test
	public void testNullDietGoals() {
		assertThrows(IllegalArgumentException.class, () -> {
			EditDietGoalsHandler.createEditDietGoalsRequest("username", null);
		});
	}

	@Test
	public void testValidEditDietGoalsRequest() throws Exception {
		String result = EditDietGoalsHandler.createEditDietGoalsRequest("username", this.createDietGoals());

		JsonNode root = JsonMapperFactory.create().readTree(result);
		JsonNode dietGoalsNode = root.get(ServerConstants.KEY_DIET_GOALS);

		assertEquals(ServerConstants.EDIT_DIET_GOALS_REQUEST_TYPE,
				root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("username", root.get(ServerConstants.KEY_USERNAME).asText());
		assertEquals(PrimaryGoal.PROTEIN.name(), dietGoalsNode.get(ServerConstants.KEY_PRIMARY_GOAL).asText());
		assertEquals(1800, dietGoalsNode.get(ServerConstants.KEY_CALORIE_GOAL).asDouble());
		assertEquals(140, dietGoalsNode.get(ServerConstants.KEY_PROTEIN_GOAL).asDouble());
		assertEquals(55, dietGoalsNode.get(ServerConstants.KEY_FAT_GOAL).asDouble());
		assertEquals(35, dietGoalsNode.get(ServerConstants.KEY_SUGAR_GOAL).asDouble());
		assertEquals(2000, dietGoalsNode.get(ServerConstants.KEY_SODIUM_GOAL).asDouble());
		assertEquals(175, dietGoalsNode.get(ServerConstants.KEY_CARBS_GOAL).asDouble());
		assertEquals("Build lean muscle", dietGoalsNode.get(ServerConstants.KEY_OTHER_GOALS).get(0).asText());
	}

	@Test
	public void testSerializationFailure() throws Exception {
		ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

		try (MockedStatic<JsonMapperFactory> mapperFactoryMock = Mockito.mockStatic(JsonMapperFactory.class)) {
			mapperFactoryMock.when(JsonMapperFactory::create).thenReturn(mapper);
			Mockito.when(mapper.valueToTree(Mockito.any(DietGoals.class))).thenReturn(Mockito.mock(JsonNode.class));
			Mockito.doThrow(new RuntimeException("serialization failed")).when(mapper)
					.writeValueAsString(Mockito.any());

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				EditDietGoalsHandler.createEditDietGoalsRequest("username", this.createDietGoals());
			});
			assertEquals("Failed to create add food request", exception.getMessage());
		}
	}

	private DietGoals createDietGoals() {
		return new DietGoals(PrimaryGoal.PROTEIN, 1800, 140, 55, 35, 2000, 175,
				Collections.singletonList("Build lean muscle"));
	}
}
