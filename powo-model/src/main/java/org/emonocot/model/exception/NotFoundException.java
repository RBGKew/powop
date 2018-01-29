package org.emonocot.model.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6418726630245494094L;

	public NotFoundException(Class<?> type, String identifier) {
		super(String.format("%s[%s] not found", type.getSimpleName(), identifier));
		log.warn(String.format("%s[%s] not found", type.getSimpleName(), identifier));
	}

	public NotFoundException(Class<?> type, long id) {
		this(type, Long.toString(id));
	}

}
