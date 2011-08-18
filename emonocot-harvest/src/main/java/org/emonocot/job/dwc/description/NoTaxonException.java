package org.emonocot.job.dwc.description;

/**
 *
 * @author ben
 *
 */
public class NoTaxonException extends DescriptionProcessingException {

    /**
     * 
     */
    private static final long serialVersionUID = 4236165074026471554L;

    /**
     * 
     * @param msg Set the message
     */
    public NoTaxonException(String msg) {
        super(msg);
    }

}
