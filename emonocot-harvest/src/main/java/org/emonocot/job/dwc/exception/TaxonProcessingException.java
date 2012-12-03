package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.RecordType;

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
     * @param code the code
     * @param value the value
     */
    public TaxonProcessingException(final String msg,
            final AnnotationCode code, final String value) {
        super(msg, code, RecordType.Taxon, value);
    }
}
