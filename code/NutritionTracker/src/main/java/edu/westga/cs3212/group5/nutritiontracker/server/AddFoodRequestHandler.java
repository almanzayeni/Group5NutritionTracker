package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

/**
 * Handles ADD_FOOD requests to the server.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class AddFoodRequestHandler {

	/**
	 * Creates the add food request JSON string to send to the server.
	 *
	 * @precondition food != null
	 *
	 * @param food the food item to add
	 * @return the request as a JSON string
	 * @throws IllegalArgumentException if food is null
	 */
	public static String createAddFoodRequest(FoodItem food) {
		if (food == null) {
			throw new IllegalArgumentException("Food cannot be null");
		}

		ObjectMapper mapper = JsonMapperFactory.create();

		try {
			JsonNode foodNode = mapper.valueToTree(food);

			Map<String, Object> requestMap = new HashMap<>();
			requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.ADD_FOOD_REQUEST_TYPE);
			requestMap.put(ServerConstants.KEY_FOOD_ITEM, foodNode);

			return mapper.writeValueAsString(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create add food request");
		}
	}

	/**
	 * Sends the add food request to the server and handles the response.
	 *
	 * @precondition request != null && !request.isBlank()
	 *
	 * @param request the JSON request string
	 * @throws IllegalArgumentException if request is null or blank
	 * @throws RuntimeException         if the server returns a failure or cannot be
	 *                                  reached
	 */
	public static void handleAddFoodRequest(String request) {
	    if (request == null || request.isBlank()) {
	        throw new IllegalArgumentException("Request cannot be null or blank");
	    }

	    ObjectMapper mapper = JsonMapperFactory.create();
	    try {
	        String response = ServerClient.send(request);

	        if (response == null || response.isBlank()) {
	            throw new RuntimeException("Received empty response from server");
	        }

	        JsonNode root = mapper.readTree(response);

	        if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
	            String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
	            throw new RuntimeException("Add food failed: " + failureMessage);
	        }

	        String status = root.get(ServerConstants.KEY_STATUS).asText();
	        if (!status.equals(ServerConstants.SUCCESS_STATUS)) {
	            throw new RuntimeException("Add food failed with status: " + status);
	        }

	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to handle add food request", e);
	    }
	}
}
