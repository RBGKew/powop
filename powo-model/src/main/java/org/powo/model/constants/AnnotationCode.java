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
package org.powo.model.constants;

/**
 *
 * @author ben
 *
 */
public enum AnnotationCode {
	/**
	 * Indicates that a record was updated.
	 */
	Update,
	/**
	 * Indicates that a record was created.
	 */
	Create,
	/**
	 * If we deleted an existing record.
	 */
	Delete,
	/**
	 * If an expected record was present.
	 */
	Present,
	/**
	 * If an expected record was not present.
	 */
	Absent,
	/**
	 * If an unexpected record was present (but ignored).
	 */
	Unexpected,
	/**
	 * If an identifier property does not resolve to an object.
	 */
	BadIdentifier,
	/**
	 * If a record was already processed.
	 */
	AlreadyProcessed,
	/**
	 * If there was a problem parsing a whole record.
	 */
	BadRecord,
	/**
	 * If there was a problem converting a (non-identifier) value of a field.
	 */
	BadField,
	/**
	 * If the record has not changed, we skip it
	 */
	Skipped,
	/**
	 * If we find that the record belongs to another organisation
	 */
	WrongAuthority,
	/**
	 * There was a problem with the data held for a record
	 */
	BadData,
	/**
	 * Indicates an associated record that should be indexed
	 */
	Index
}
