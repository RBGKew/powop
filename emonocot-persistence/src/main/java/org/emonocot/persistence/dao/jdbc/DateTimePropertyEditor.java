/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.persistence.dao.jdbc;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author jk00kg
 *
 */
public class DateTimePropertyEditor extends PropertyEditorSupport {

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(final Object value) {
		if(value instanceof Date) {
			super.setValue(new DateTime(value));
		} else {
			super.setValue(value);
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {
		return super.getValue();
	}
}
