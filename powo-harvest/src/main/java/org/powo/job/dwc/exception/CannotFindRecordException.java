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
package org.powo.job.dwc.exception;

import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;

/**
 *
 * @author ben
 *
 */
public class CannotFindRecordException extends TaxonProcessingException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3822333603663281893L;

	/**
	 *
	 * @param identifier the identifier of the object
	 * @param title
	 */
	public CannotFindRecordException(final String identifier, final String title) {
		super("Cannot find a taxon " + title + " with identifier " + identifier, AnnotationCode.BadIdentifier, identifier);
	}

	public CannotFindRecordException(final String identifier) {
		super("Cannot find a taxon with identifier " + identifier, AnnotationCode.BadIdentifier, identifier);
	}

	@Override
	public final AnnotationType getType() {
		return AnnotationType.Error;
	}

}
