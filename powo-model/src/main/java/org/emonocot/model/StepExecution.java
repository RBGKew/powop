package org.emonocot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Mixin to expose subset of fields via Jackson
 * see https://github.com/FasterXML/jackson-docs/wiki/JacksonMixInAnnotations
 */
@JsonIgnoreProperties(value = {
		"jobExecution",
		"jobParameters",
})
public interface StepExecution { }
