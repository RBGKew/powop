package org.emonocot.checklist.persistence;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.persistence.pager.Page;
import org.openarchives.pmh.SetSpec;

public interface IdentifiableService<T extends IdentifiableEntity> {
	
	/**
	 * Find an object with the given identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public ChangeEvent<T> find(Serializable identifier);
	
	public Page<ChangeEvent<T>> page(SetSpec set, DateTime from, DateTime until, Integer pageSize, Integer pageNumber);

}
