package org.emonocot.job.dwc.taxon;

/**
 *
 * @author ben
 *
 */
public class TaxonProcessingException extends RuntimeException {

    /**
     *
     * @param msg the message
     */
    public TaxonProcessingException(final String msg) {
        super(msg);
    }

    /**
     *
     */
    private static final long serialVersionUID = 999609749452018246L;

    /**
     *
     * @return a short code representing this class of error
     */
    public String getCode() {
        return this.getClass().getSimpleName().toUpperCase();
    }

}
