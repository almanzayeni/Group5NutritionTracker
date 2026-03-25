package edu.westga.cs3212.group5.nutritiontracker.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * A factory for creating JsonMapper objects.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public final class JsonMapperFactory {

	/**
	 * Creates the ObjectMapper with JavaTimeModuel.
	 *
	 * @return the ObjectMapper
	 */
	public static ObjectMapper create() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}
}
