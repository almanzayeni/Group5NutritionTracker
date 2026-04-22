package edu.westga.cs3212.group5.nutritiontracker.server.getdayoffoodrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.server.GetDayOfFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateRequest {

	@Test
	void testCreateRequestNullUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.createRequest(null, LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testCreateRequestBlankUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.createRequest("   ", LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testCreateRequestNullDateThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> GetDayOfFoodRequestHandler.createRequest("username", null));
	}

	@Test
	void testCreateRequestValidValuesProducesExpectedJson() throws Exception {
		LocalDate date = LocalDate.of(2026, 4, 21);

		String request = GetDayOfFoodRequestHandler.createRequest("username", date);
		JsonNode root = new ObjectMapper().readTree(request);

		assertEquals(ServerConstants.GET_DAY_OF_FOOD_REQUEST_TYPE, root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("username", root.get(ServerConstants.KEY_USERNAME).asText());
		assertEquals(date.toString(), root.get(ServerConstants.KEY_DATE).asText());
	}
}
