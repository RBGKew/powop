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
package org.emonocot.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class Base implements Serializable, Identifiable, SecuredObject {

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(Base.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 4778611345983453363L;

	/**
	 *
	 */
	protected String identifier;

	@Override
	public boolean equals(Object other) {
		// check for self-comparison
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		// Only works when classes are instantiated
		if ((other.getClass().equals(this.getClass()))) {

			Base base = (Base) other;
			if (this.getIdentifier() == null && base.getIdentifier() == null) {

				if (this.getId() != null && base.getId() != null) {
					return ObjectUtils.equals(this.getId(), base.getId());
				} else {
					return false;
				}
			} else {
				return ObjectUtils.equals(this.identifier, base.identifier);
			}
		} else if (HibernateProxyHelper.getClassWithoutInitializingProxy(other)
				.equals(this.getClass())) {
			// Case to check when proxies are involved
			Identifiable base = (Identifiable) other;

			if (this.getIdentifier() == null && base.getIdentifier() == null) {
				return false;
			} else {
				return ObjectUtils
						.equals(this.identifier, base.getIdentifier());
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this.identifier);
	}

}
