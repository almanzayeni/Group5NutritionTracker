package edu.westga.cs3212.group5.nutritiontracker.server.searchreqeusthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.SearchRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestHandleSearchRequest {

	@Test
	public void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			SearchRequestHandler.handleSearchRequest(null);
		});
	}

	@Test
	public void testBlankRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			SearchRequestHandler.handleSearchRequest("   ");
		});
	}

	@Test
	public void testNullResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(null);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	@Test
	public void testBlankResponse() {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn("   ");

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	@Test
	public void testFailureMessageResponse() {
		String response = "{\"failure_message\":\"food not found\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	@Test
	public void testMissingSearchResultsResponse() {
		String response = "{\"status\":\"" + ServerConstants.SUCCESS_STATUS + "\"}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	@Test
	public void testSearchResultsIsNotArray() {
		String response = "{\"" + ServerConstants.KEY_SEARCH_RESULTS + "\":{}}";
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	@Test
	public void testEmptySearchResults() throws Exception {
		String response = JsonMapperFactory.create()
				.writeValueAsString(Map.of(ServerConstants.KEY_SEARCH_RESULTS, Collections.emptyList()));
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			List<FoodItem> results = SearchRequestHandler.handleSearchRequest("request");

			assertEquals(0, results.size());
		}
	}

	@Test
	public void testMixedBaseAndCompositeSearchResults() throws Exception {
		BaseFood banana = new BaseFood("banana", QuantityCategory.QUANTITY, 1, 105, 1, 0, 14, 27, 1);
		BaseFood oats = new BaseFood("oats", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0);
		CompositeFood bananaOatmeal = new CompositeFood("banana oatmeal", QuantityCategory.SERVING, 1,
				List.of(banana, oats));

		String response = this.createSearchResultsResponse(List.of(banana, bananaOatmeal));
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenReturn(response);

			List<FoodItem> results = SearchRequestHandler.handleSearchRequest("request");

			assertEquals(2, results.size());
			assertTrue(results.get(0) instanceof BaseFood);
			assertEquals("banana", results.get(0).getDescription());
			assertTrue(results.get(1) instanceof CompositeFood);
			assertEquals("banana oatmeal", results.get(1).getDescription());
			CompositeFood compositeFood = (CompositeFood) results.get(1);
			assertEquals(2, compositeFood.getIngredients().size());
			assertTrue(compositeFood.getIngredients().containsKey("banana"));
			assertTrue(compositeFood.getIngredients().containsKey("oats"));
		}
	}

	@Test
	public void testServerThrowsException() throws Exception {
		try (MockedStatic<ServerClient> serverClientMock = Mockito.mockStatic(ServerClient.class)) {
			serverClientMock.when(() -> ServerClient.send("request")).thenThrow(new Exception("connection refused"));

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.handleSearchRequest("request");
			});
			assertEquals("Failed to handle search request", exception.getMessage());
			assertNull(exception.getCause());
		}
	}

	private String createSearchResultsResponse(List<FoodItem> foodItems) throws Exception {
		var mapper = JsonMapperFactory.create();
		var resultsNode = mapper.createArrayNode();

		for (FoodItem foodItem : foodItems) {
			resultsNode.add(mapper.readTree(mapper.writerFor(FoodItem.class).writeValueAsString(foodItem)));
		}

		var root = mapper.createObjectNode();
		root.set(ServerConstants.KEY_SEARCH_RESULTS, resultsNode);
		return mapper.writeValueAsString(root);
	}
}
