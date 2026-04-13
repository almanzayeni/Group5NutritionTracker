package edu.westga.cs3212.group5.nutritiontracker.server.addfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateAddFoodRequest {

	private static FoodItem makeFood() {
		return new BaseFood("oatmeal", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0);
	}

	@Test
	public void testNullFood() {
		assertThrows(IllegalArgumentException.class, () -> {
			AddFoodRequestHandler.createAddFoodRequest(null);
		});
	}

	@Test
	public void testValidAddFoodRequestContainsRequestType() throws Exception {
		String request = AddFoodRequestHandler.createAddFoodRequest(makeFood());
		JsonNode root = new ObjectMapper().readTree(request);

		assertEquals(ServerConstants.ADD_FOOD_REQUEST_TYPE, root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
	}

	@Test
	public void testValidAddFoodRequestContainsSerializedFoodItem() throws Exception {
		String request = AddFoodRequestHandler.createAddFoodRequest(makeFood());
		JsonNode root = new ObjectMapper().readTree(request);
		JsonNode foodNode = root.get(ServerConstants.KEY_FOOD_ITEM);

		assertEquals(ServerConstants.KEY_BASE_FOOD_TYPE, foodNode.get(ServerConstants.KEY_FOOD_TYPE).asText());
		assertEquals("oatmeal", foodNode.get(ServerConstants.KEY_FOOD_DESCRIPTION).asText());
		assertEquals(QuantityCategory.SERVING.name(),
				foodNode.get(ServerConstants.KEY_FOOD_QUANTITY_CATEGORY).asText());
	}

	@Test
	public void testSerializationFailure() throws Exception {
		ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

		try (MockedStatic<JsonMapperFactory> mapperFactoryMock = Mockito.mockStatic(JsonMapperFactory.class)) {
			mapperFactoryMock.when(JsonMapperFactory::create).thenReturn(mapper);
			Mockito.doThrow(new RuntimeException("serialization failed")).when(mapper)
					.writeValueAsString(Mockito.any());

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				AddFoodRequestHandler.createAddFoodRequest(makeFood());
			});
			assertEquals("Failed to create add food request", exception.getMessage());
		}
	}
}
