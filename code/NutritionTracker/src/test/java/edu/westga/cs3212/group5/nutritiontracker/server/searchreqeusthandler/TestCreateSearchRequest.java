package edu.westga.cs3212.group5.nutritiontracker.server.searchreqeusthandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.server.SearchRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestCreateSearchRequest {

	@Test
	public void testNullQuery() {
		assertThrows(IllegalArgumentException.class, () -> {
			SearchRequestHandler.createSearchRequest(null);
		});
	}

	@Test
	public void testValidSearchRequest() throws Exception {
		String result = SearchRequestHandler.createSearchRequest("banana");
		JsonNode root = new ObjectMapper().readTree(result);

		assertEquals(ServerConstants.SEARCH_REQUEST_TYPE, root.get(ServerConstants.KEY_REQUEST_TYPE).asText());
		assertEquals("banana", root.get(ServerConstants.KEY_QUERY).asText());
	}

	@Test
	public void testSerializationFailure() throws Exception {
		ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

		try (MockedStatic<JsonMapperFactory> mapperFactoryMock = Mockito.mockStatic(JsonMapperFactory.class)) {
			mapperFactoryMock.when(JsonMapperFactory::create).thenReturn(mapper);
			Mockito.doThrow(new RuntimeException("serialization failed")).when(mapper)
					.writeValueAsString(Mockito.any());

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				SearchRequestHandler.createSearchRequest("banana");
			});
			assertEquals("Failed to create search request", exception.getMessage());
		}
	}
}
