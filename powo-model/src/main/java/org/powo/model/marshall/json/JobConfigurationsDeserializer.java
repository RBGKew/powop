package org.powo.model.marshall.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.powo.api.JobConfigurationService;
import org.powo.model.JobConfiguration;
import org.powo.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JobConfigurationsDeserializer extends StdDeserializer<List<JobConfiguration>> {

	private static final long serialVersionUID = -1569163219254079157L;

	private static final Logger logger = LoggerFactory.getLogger(JobConfigurationsDeserializer.class);

	@Autowired
	private JobConfigurationService jobConfigurationService;

	public JobConfigurationsDeserializer(Class<?> vc) {
		super(vc);
	}

	public JobConfigurationsDeserializer() {
		this(JobConfiguration.class);
	}

	@Override
	public List<JobConfiguration> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		List<JobConfiguration> jobs = new ArrayList<>();
		JsonNode node = p.readValueAsTree();
		System.out.println(node);
		if (node.isArray()) {
			for (JsonNode config : node) {
				JobConfiguration job = null;
				String identifier = null;
				if(config.hasNonNull("identifier")) {
					identifier = config.get("identifier").asText();
				} else {
					identifier = config.asText();
				}

				if (jobConfigurationService != null) {
					try {
						job = jobConfigurationService.get(identifier);
					} catch (NotFoundException e) {
						logger.info("Couldn't find job configuration with id: {}", identifier);
					}
				}

				if (job == null) {
					jobs.add(JobConfiguration.builder().identifier(identifier).build());
				}

				jobs.add(job);
			}
		}

		return jobs;
	}
}
