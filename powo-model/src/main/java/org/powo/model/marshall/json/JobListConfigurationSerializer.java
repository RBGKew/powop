package org.powo.model.marshall.json;

import java.io.IOException;
import java.util.List;

import org.powo.model.JobConfiguration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JobListConfigurationSerializer extends JsonSerializer<List<JobConfiguration>> {

	@Override
	public void serialize(List<JobConfiguration> jobConfigurations, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeStartArray();
		for(JobConfiguration jobConfiguration : jobConfigurations) {
			gen.writeObject(jobConfiguration.getIdentifier());
		}
		gen.writeEndArray();
	}
}
