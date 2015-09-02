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

import java.net.URL;

import org.emonocot.model.SearchableObject;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class SitemapProcessor implements ItemProcessor<SearchableObject, Url>, Converter<SearchableObject, Url> {

	private static final Logger logger = LoggerFactory.getLogger(SitemapProcessor.class);

	String portalBaseUrl;

	private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

	public void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Url process(SearchableObject item) throws Exception {
		logger.debug("Trying to create portal URL for " + item.getClassName() + " with identifier: " + item.getIdentifier());
		Url url = new Url();
		if(item.getModified() != null){
			url.setLastmod(dateTimeFormatter.print(item.getModified()));
		} else if (item.getCreated() != null) {
			url.setLastmod(dateTimeFormatter.print(item.getCreated()));
		}

		if ("Taxon".equals(item.getClassName())) {
			url.setLoc(new URL(portalBaseUrl + "/taxon/" + item.getIdentifier()));
		} else if ("Image".equals(item.getClassName())) {
			url.setLoc(new URL(portalBaseUrl + "/image/" + item.getId()));
		} else if ("IdentificationKey".equals(item.getClassName())) {
			url.setLoc(new URL(portalBaseUrl + "/key/" + item.getId()));
		} else {
			logger.warn("Not writing url for object", new IllegalArgumentException("Unable to process object of type " + item.getClassName() + " and identifier:" + item.getIdentifier()));
			url = null;
		}

		return url;
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Url convert(SearchableObject object) {
		try {
			return process(object);
		} catch (Exception e) {
			logger.error("Unable convert Url from object", e);
			return null;
		}
	}

}
