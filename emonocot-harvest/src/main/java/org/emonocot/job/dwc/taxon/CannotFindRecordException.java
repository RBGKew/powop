package org.emonocot.job.dwc.taxon;

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
     */
    public CannotFindRecordException(final String identifier) {
        super("Cannot find a taxon with identifier " + identifier);
    }

}
