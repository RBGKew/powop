package org.emonocot.checklist.model;

import org.joda.time.DateTime;

public interface ChangeEvent<T extends IdentifiableEntity> {

	T getObject();

	ChangeType getType();
	
	DateTime getDatestamp();

}
