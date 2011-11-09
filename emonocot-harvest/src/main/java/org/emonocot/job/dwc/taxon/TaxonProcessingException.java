package org.emonocot.job.dwc.taxon;

import org.emonocot.job.dwc.DarwinCoreProcessingException;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.RecordType;

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
