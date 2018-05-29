package org.powo.model.marshall.json;

import java.io.IOException;
import java.util.List;

import org.powo.model.JobConfiguration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class JobListConfigurationSerializer extends JsonSerializer<List<JobConfiguration>> {

	@Getter
	@AllArgsConstructor
	private class Summary {
		private String identifier;
	}

	@Override
	public void serialize(List<JobConfiguration> jobConfigurations, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		Summary[] summaries = jobConfigurations.stream()
				.map(jc -> new Summary(jc.getIdentifier()))
				.toArray(Summary[]::new);

		gen.writeStartArray();

		for(Summary summary : summaries) {
			gen.writeObject(summary);
		}

		gen.writeEndArray();
	}


}
