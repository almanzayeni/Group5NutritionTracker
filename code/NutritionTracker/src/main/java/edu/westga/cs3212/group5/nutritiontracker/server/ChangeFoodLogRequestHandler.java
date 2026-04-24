package edu.westga.cs3212.group5.nutritiontracker.server;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

/**
 * Handles DayOfFood requests.
 * @author Emi Collins
 */
public class ChangeFoodLogRequestHandler {
	/**
	 * Creates the day of food request.
	 */
	public static String createRequest(String username, String password, LocalDate date) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or blank");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.GET_DAY_OF_FOOD_REQUEST_TYPE);
        requestMap.put(ServerConstants.KEY_USERNAME, username);
        requestMap.put(ServerConstants.KEY_PASSWORD, password);
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
	public static FoodLog handleRequest(String username, String password, LocalDate date) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or blank");
		}
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        ObjectMapper mapper = JsonMapperFactory.create();

        try {
            String request = createRequest(username, password, date);

            String response = ServerClient.send(request);
            if (response == null || response.isBlank()) {
                throw new RuntimeException("Received empty response from server");
            }

            var root = mapper.readTree(response);

            if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
                String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
                throw new RuntimeException("DayOfFood failed: " + failureMessage);
            }

            if (!root.has(ServerConstants.KEY_FOOD_LOG)) {
                throw new RuntimeException("FoodLog missing from response");
            }

            FoodLog log = mapper.treeToValue(root.get(ServerConstants.KEY_FOOD_LOG), FoodLog.class);
            return log;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to handle DayOfFood request", e);
        }
    }
}
