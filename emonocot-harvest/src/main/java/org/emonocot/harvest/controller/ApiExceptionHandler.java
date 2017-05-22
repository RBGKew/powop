package org.emonocot.harvest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.emonocot.model.exception.InvalidEntityException;
import org.emonocot.model.exception.NotFoundException;
import org.emonocot.model.marshall.json.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class); 

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex, WebRequest request) {
		log.debug("handling not found exception");
		ApiErrorResponse response = ApiErrorResponse.builder()
				.error("NotFound")
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidEntityException.class)
	protected ResponseEntity<ApiErrorResponse> handleInvalidEntity(InvalidEntityException ex, WebRequest request) {
		log.debug("handling invalid entity exception");
		ApiErrorResponse response = ApiErrorResponse.builder()
				.error("Invalid")
				.validationErrors(ex.getErrors())
				.build();

		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
