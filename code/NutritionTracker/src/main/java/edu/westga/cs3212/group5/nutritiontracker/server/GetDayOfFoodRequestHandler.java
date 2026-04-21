package edu.westga.cs3212.group5.nutritiontracker.server;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

/**
 * Handles DayOfFood requests.
 * @author Emi Collins
 */
public class GetDayOfFoodRequestHandler {
	/**
	 * Creates the day of food request.
	 */
	public static String createRequest(String username, LocalDate date) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.GET_DAY_OF_FOOD_REQUEST_TYPE);
        requestMap.put(ServerConstants.KEY_USERNAME, username);
        requestMap.put(ServerConstants.KEY_DATE, date.toString());
        
        try {
            return new ObjectMapper().writeValueAsString(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create day-of-food request");
        }
	}
	
	/**
	 * Sends request and returns FoodLog
	 */
	public static FoodLog handleRequest(String username, LocalDate date) {
	    if (username == null || username.isBlank()) {
	        throw new IllegalArgumentException("Username cannot be null or blank");
	    }
	    if (date == null) {
	        throw new IllegalArgumentException("Date cannot be null");
	    }

	    ObjectMapper mapper = JsonMapperFactory.create();
	    try {
	        Map<String, Object> requestMap = new HashMap<>();
	        requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.GET_DAY_OF_FOOD_REQUEST_TYPE);
	        requestMap.put(ServerConstants.KEY_USERNAME, username);
	        requestMap.put(ServerConstants.KEY_DATE, date.toString());

	        String request = mapper.writeValueAsString(requestMap);
	        String response = ServerClient.send(request);

	        if (response == null || response.isBlank()) {
	            throw new RuntimeException("Received empty response from server");
	        }

	        JsonNode root = mapper.readTree(response);

	        if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
	            throw new RuntimeException("Get food log failed: " + root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText());
	        }

	        String status = root.get(ServerConstants.KEY_STATUS).asText();
	        if (!status.equals(ServerConstants.SUCCESS_STATUS)) {
	            throw new RuntimeException("Get food log failed with status: " + status);
	        }

	        JsonNode logNode = root.get(ServerConstants.KEY_FOOD_LOG);
	        FoodLog foodLog = new FoodLog(date);

	        for (JsonNode item : logNode.get("breakfast")) {
	            foodLog.getBreakfast().add(deserializeFoodItem(item));
	        }
	        for (JsonNode item : logNode.get("lunch")) {
	            foodLog.getLunch().add(deserializeFoodItem(item));
	        }
	        for (JsonNode item : logNode.get("dinner")) {
	            foodLog.getDinner().add(deserializeFoodItem(item));
	        }
	        for (JsonNode item : logNode.get("snacks")) {
	            foodLog.getSnacks().add(deserializeFoodItem(item));
	        }

	        return foodLog;

	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to handle get day of food request", e);
	    }
	}
	
	private static FoodItem deserializeFoodItem(JsonNode node) throws Exception {
	    ObjectMapper mapper = JsonMapperFactory.create();
	    return mapper.treeToValue(node, FoodItem.class);
	}
}
