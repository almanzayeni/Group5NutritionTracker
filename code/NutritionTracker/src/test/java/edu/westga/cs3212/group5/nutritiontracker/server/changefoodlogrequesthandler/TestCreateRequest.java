package edu.westga.cs3212.group5.nutritiontracker.server.changefoodlogrequesthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.server.ChangeFoodLogRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateRequest {

	@Test
	void testCreateRequestNullUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ChangeFoodLogRequestHandler.createRequest(null, "password", LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testCreateRequestBlankUsernameThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ChangeFoodLogRequestHandler.createRequest("   ", "password", LocalDate.of(2026, 4, 21)));
	}
	
	@Test
	void testCreateRequestNullPasswordThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ChangeFoodLogRequestHandler.createRequest("username", null, LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testCreateRequestBlankPasswordThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ChangeFoodLogRequestHandler.createRequest("username", " ", LocalDate.of(2026, 4, 21)));
	}

	@Test
	void testCreateRequestNullDateThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ChangeFoodLogRequestHandler.createRequest("username", "password", null));
	}

	@Test
	void testCreateRequestValidValuesProducesExpectedJson() throws Exception {
		LocalDate date = LocalDate.of(2026, 4, 21);

		String request = ChangeFoodLogRequestHandler.createRequest("username", "passowrd", date);
		JsonNode root = new ObjectMapper().readTree(request);

		assertEquals(ServerConstants.GET_DAY_OF_FOOD_REQUEST_TYPE, root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("username", root.get(ServerConstants.KEY_USERNAME).asText());
		assertEquals("passowrd", root.get(ServerConstants.KEY_PASSWORD).asText());
		assertEquals(date.toString(), root.get(ServerConstants.KEY_DATE).asText());
	}
}
