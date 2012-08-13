/**
 * 
 */
package org.emonocot.portal.model;

import java.net.URL;

import org.emonocot.model.common.SearchableObject;
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
public class PortalUrlMapper implements ItemProcessor<SearchableObject, Url>, Converter<SearchableObject, Url> {
	
	/**
    *
    */
   private Logger logger = LoggerFactory.getLogger(PortalUrlMapper.class);

	/**
	 * 
	 */
	String portalBaseUrl;

    /**
     *
     */
    private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

	/**
	 * @param portalBaseUrl the portalBaseUrl to set
	 */
	public final void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Url process(SearchableObject item) throws Exception {
		Url url = new Url();
		url.setLastmod(dateTimeFormatter.print(item.getModified()));
		
		switch (item.getClassName()) {
		case "Taxon":
				url.setLoc(new URL(portalBaseUrl + "/taxon/" + item.getIdentifier()));
			break;
		case "Image":
			url.setLoc(new URL(portalBaseUrl + "/image/" + item.getIdentifier()));
			break;
		case "IdentificationKey":
			url.setLoc(new URL(portalBaseUrl + "/key/" + item.getIdentifier()));
			break;
		default:
			throw new Exception("Unable to process object of type " + item.getClassName() + " and identifier:" + item.getIdentifier());
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
