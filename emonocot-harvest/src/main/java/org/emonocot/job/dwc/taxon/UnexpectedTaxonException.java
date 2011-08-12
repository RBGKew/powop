package org.emonocot.job.dwc.taxon;

/**
 *
 * @author ben
 *
 */
public class UnexpectedTaxonException extends TaxonProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = -79759942758744782L;

    /**
     *
     * @param identifier the identifier of the unexpected taxon
     */
    public UnexpectedTaxonException(final String identifier) {
        super("Found unexpected taxon with identifier " + identifier);
    }

}
