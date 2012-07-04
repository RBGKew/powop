package org.emonocot.checklist.view;

import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public final class Functions {
	/**
	 *
	 */
	public static String FAMILY_ID_PREFIX = "urn:kew.org:wcs:family:";
	
	/**
	 *
	 */
	public static String FAMILY_NAME_PREFIX = "urn:kew.org:wcs:familyName:";

	/**
	 *
	 */
	public static String CHECKLIST_ID_PREFIX = "urn:kew.org:wcs:taxon:";
	
	/**
	 *
	 */
	public static String CHECKLIST_NAME_PREFIX = "urn:kew.org:wcs:name:";

    /**
     *
     */
    private Functions() {
    }

    /**
     *
     * @param string Set the string to escape
     * @return an escaped string
     */
    public static String escape(final String string) {
        return string.replaceAll("&", "&amp;");
    }
    
    public static String nameIdentifier(final Taxon taxon) {
    	if(taxon == null || taxon.getIdentifier() == null) {
    		return null;
    	} else if(taxon.getIdentifier().startsWith(FAMILY_ID_PREFIX)) {
    		return FAMILY_NAME_PREFIX + taxon.getIdentifier().substring(FAMILY_ID_PREFIX.length());
    	} else if(taxon.getIdentifier().startsWith(CHECKLIST_ID_PREFIX)) {
    		return CHECKLIST_NAME_PREFIX + taxon.getIdentifier().substring(CHECKLIST_ID_PREFIX.length());
    	} else {
    		return "NAME" + taxon.getIdentifier();
    	}
    }
}
