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
package org.emonocot.job.sitemap;

import java.io.Serializable;
import java.net.URL;

/**
 * @author jk00kg
 * A URL object found in a sitemap.xml document
 */
public class Url implements Serializable {

	/**
	 * 
	 */
	private URL loc;
	
	/**
	 * 
	 */
	private String lastmod;

	/**
	 * @return the loc
	 */
	public final URL getLoc() {
		return loc;
	}

	/**
	 * @param loc the loc to set
	 */
	public final void setLoc(URL loc) {
		this.loc = loc;
	}

	/**
	 * @return the lastmod
	 */
	public final String getLastmod() {
		return lastmod;
	}

	/**
	 * @param lastmod the lastmod to set
	 */
	public final void setLastmod(String lastmod) {
		this.lastmod = lastmod;
	}
}
