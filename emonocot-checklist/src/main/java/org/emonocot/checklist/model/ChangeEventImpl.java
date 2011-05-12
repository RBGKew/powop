package org.emonocot.checklist.model;

import org.joda.time.DateTime;


public class ChangeEventImpl<T extends IdentifiableEntity> implements ChangeEvent<T> {
	private T object;
	private ChangeType type;
	private DateTime date;

	public ChangeEventImpl(T object, ChangeType type, DateTime date) {
		this.object = object;
		this.type = type;
		this.date = date;
	}

	public T getObject() {
		return object;
	}

    public ChangeType getType() {
		return type;
	}

	public DateTime getDatestamp() {
		return date;
	}

}
