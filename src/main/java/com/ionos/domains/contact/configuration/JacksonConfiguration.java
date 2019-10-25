package com.ionos.domains.contact.configuration;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

@Configuration
public class JacksonConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		// @formatter:off
		return new ObjectMapper().configure(WRITE_DATES_AS_TIMESTAMPS, false)
				.configure(FAIL_ON_UNKNOWN_PROPERTIES, false).configure(USE_BIG_DECIMAL_FOR_FLOATS, true)
				.setSerializationInclusion(NON_EMPTY).registerModule(new Jdk8Module());
		// @formatter:on
	}
}
