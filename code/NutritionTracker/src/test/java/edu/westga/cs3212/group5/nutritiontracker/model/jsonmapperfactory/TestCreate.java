package edu.westga.cs3212.group5.nutritiontracker.model.jsonmapperfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

public class TestCreate {

	@Test
	public void testCreateReturnsConfiguredMapper() {
		ObjectMapper mapper = JsonMapperFactory.create();
		assertNotNull(mapper);
		assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
	}

	@Test
	public void testCreateSupportsSerializingLocalDateAsIsoString() throws Exception {
		ObjectMapper mapper = JsonMapperFactory.create();
		String result = mapper.writeValueAsString(LocalDate.of(2026, 3, 25));
		assertEquals("\"2026-03-25\"", result);
	}

	@Test
	public void testCreateSupportsDeserializingLocalDate() throws Exception {
		ObjectMapper mapper = JsonMapperFactory.create();
		LocalDate result = mapper.readValue("\"2026-03-25\"", LocalDate.class);
		assertEquals(LocalDate.of(2026, 3, 25), result);
	}

}
