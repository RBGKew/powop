package org.emonocot.model.marshall.json;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@Builder
public class ApiErrorResponse {
	private String type;
	private String message;
	private Map<String, String> validationErrors;
}
