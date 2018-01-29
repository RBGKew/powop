package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.api.JobConfigurationService;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JobConfigurationsDeserializer extends StdDeserializer<List<JobConfiguration>> {

	private static final long serialVersionUID = -1569163219254079157L;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	public JobConfigurationsDeserializer(Class<?> vc) {
		super(vc);
	}

	public JobConfigurationsDeserializer() {
		this(null);
	}

	@Override
	public List<JobConfiguration> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		List<JobConfiguration> jobs = new ArrayList<>();
		JsonToken t = p.getCurrentToken();

		if(t == JsonToken.START_ARRAY) {
			t = p.nextToken();
			while(t != JsonToken.END_ARRAY) {
				Long id = _parseLong(p, ctxt);
				try {
					jobs.add(jobConfigurationService.get(id));
				} catch(NotFoundException e) {
					jobs.add(JobConfiguration.builder().id(id).build());
				}
				t = p.nextToken();
			}
		}

		return jobs;
	}
}
