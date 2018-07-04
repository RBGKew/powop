package org.powo.portal.controller;

import org.powo.model.exception.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	@ExceptionHandler(value = NotFoundException.class)
	public String notFound() {
		return "forward:/not-found-error";
	}
}
