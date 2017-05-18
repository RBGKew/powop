package org.emonocot.persistence.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.Getter;

@Getter
public class InvalidEntityException extends RuntimeException {

	private static final long serialVersionUID = -8015376814021466689L;

	private Map<String, String> errors;

	public InvalidEntityException(Class<?> type, BindingResult binding) {
		errors = new HashMap<>();

		for(FieldError error : binding.getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
	}
}
