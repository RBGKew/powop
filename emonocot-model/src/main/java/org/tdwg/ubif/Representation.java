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
package org.tdwg.ubif;

import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public class Representation {

	/**
	 *
	 */
	@XStreamAlias("Label")
	private String label;

	/**
	 *
	 */
	@XStreamAlias("Detail")
	private String detail;

	/**
	 *
	 */
	@XStreamImplicit(itemFieldName="MediaObject")
	private Set<MediaObjectRef> mediaObjects;

	/**
	 *
	 * @param newLabel Set the label
	 */
	public final void setLabel(final String newLabel) {
		this.label = newLabel;
	}

	/**
	 *
	 * @return the label
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @return the mediaObjects
	 */
	public final Set<MediaObjectRef> getMediaObjects() {
		return mediaObjects;
	}

	/**
	 * @param newMediaObjects the mediaObject to set
	 */
	public final void setMediaObjects(final Set<MediaObjectRef> newMediaObjects) {
		this.mediaObjects = newMediaObjects;
	}

	/**
	 * @return the detail
	 */
	public final String getDetail() {
		return detail;
	}

	/**
	 * @param newDetail the detail to set
	 */
	public final void setDetail(final String newDetail) {
		this.detail = newDetail;
	}
}
