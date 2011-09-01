package org.emonocot.job.dwc.taxon;

import org.emonocot.job.dwc.DarwinCoreProcessingException;

/**
 *
 * @author ben
 *
 */
public abstract class TaxonProcessingException extends
        DarwinCoreProcessingException {

   /**
    *
    */
   private static final long serialVersionUID = 999609749452018246L;

    /**
     *
     * @param msg the message
     */
    public TaxonProcessingException(final String msg) {
        super(msg);
    }
}
