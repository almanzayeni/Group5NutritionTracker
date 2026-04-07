package edu.westga.cs3212.group5.nutritiontracker.server.createfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.server.CreateAccountRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.CreateFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateFoodRequest {
	@Test
	void testCreateFoodSuccess() {
	    String request = CreateFoodRequestHandler.createFoodRequest(
	        "greek yogurt",
	        ServerConstants.QUANTITY_CATEGORY_SERVING,
	        1, 100, 17, 0, 6, 6, 65
	    );
	    
	    assertDoesNotThrow(() -> CreateFoodRequestHandler.handleCreateFoodRequest(request));
	}
	
	@Test
	void testCreateFoodDuplicate() {
	    String request = CreateFoodRequestHandler.createFoodRequest(
	        "banana",
	        ServerConstants.QUANTITY_CATEGORY_QUANTITY,
	        1, 105, 1, 0, 14, 27, 1
	    );
	    
	    assertThrows(RuntimeException.class, () -> CreateFoodRequestHandler.handleCreateFoodRequest(request));
	}
}
