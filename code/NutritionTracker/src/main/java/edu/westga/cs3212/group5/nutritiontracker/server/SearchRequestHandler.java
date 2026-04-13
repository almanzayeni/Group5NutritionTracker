package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

/**
 * The Class SearchRequestHandler.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class SearchRequestHandler {
	/**
	 * Creates the search request.
	 * 
	 * @precondition query != null && !query.isBlank()
	 *
	 * @param query the search query
	 * @return the search request as a JSON string
	 * @throws IllegalArgumentException if query is null or blank
	 */
	public static String createSearchRequest(String query) {
		if (query == null || query.isBlank()) {
			throw new IllegalArgumentException("Query cannot be null or blank");
		}

		Map<String, String> requestMap = new HashMap<>();
		requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.SEARCH_REQUEST_TYPE);
		requestMap.put(ServerConstants.KEY_QUERY, query);
		
		ObjectMapper mapper = JsonMapperFactory.create();
		
		try {
			return mapper.writeValueAsString(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create search request");
		}
	}
	
	/**
	 * Handle search request.
	 * 
	 * @precondition request != null && !request.isBlank()
	 *
	 * @param request the search request JSON String
	 * @return a list of food items matching the search query or an empty list if no matches are found
	 * @throws IllegalArgumentException if request is null or blank
	 * @throws RuntimeException if search fails or response is invalid
	 */
	public static List<FoodItem> handleSearchRequest(String request) {
		if (request == null || request.isBlank()) {
			throw new IllegalArgumentException("Request cannot be null or blank");
		}

		ObjectMapper mapper = JsonMapperFactory.create();
		List<FoodItem> searchResults = new ArrayList<FoodItem>();
		
		try {
			String response = ServerClient.send(request);
			if (response == null || response.isBlank()) {
				throw new RuntimeException("Received empty response from server");
			}

			var root = mapper.readTree(response);

			if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
				String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
				throw new RuntimeException("Search failed: " + failureMessage);
			}

			if (!root.has(ServerConstants.KEY_SEARCH_RESULTS)) {
				throw new RuntimeException("Search failed: Search Results not found in response");
			}
			
			var resultsNode = root.get(ServerConstants.KEY_SEARCH_RESULTS);
			
			if (!resultsNode.isArray()) {
				throw new RuntimeException("Search failed: Search Results is not an array");
			}
			
			for (var itemNode : resultsNode) {
				FoodItem foodItem = mapper.treeToValue(itemNode, FoodItem.class);
				searchResults.add(foodItem);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to handle search request");
		}
		
		return searchResults;
	}
}
