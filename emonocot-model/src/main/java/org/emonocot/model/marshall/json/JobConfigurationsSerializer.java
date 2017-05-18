package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.List;

import org.emonocot.model.JobConfiguration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class JobConfigurationsSerializer extends JsonSerializer<List<JobConfiguration>> {

	@Getter
	@AllArgsConstructor
	private class Summary {
		private Long id;
		private String description;
	}

	@Override
	public void serialize(List<JobConfiguration> jobConfigurations, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		Summary[] summaries = jobConfigurations.stream()
				.map(jc -> new Summary(jc.getId(), jc.getDescription()))
				.toArray(Summary[]::new);

		gen.writeStartArray();

		for(Summary summary : summaries) {
			gen.writeObject(summary);
		}

		gen.writeEndArray();
	}

}
