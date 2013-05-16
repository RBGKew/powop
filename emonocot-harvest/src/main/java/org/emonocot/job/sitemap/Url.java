/**
 * 
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
