package org.emonocot.job.dwc;

import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;

/**
 *
 * @author ben
 *
 */
public class NoTaxonException extends DarwinCoreProcessingException {

    /**
     *
     */
    private static final long serialVersionUID = 4236165074026471554L;

    /**
     *
     * @param msg Set the message
     * @param recordType the type of object missing a taxon
     * @param lineNumber the record number with a missing taxon identifier
     */
    public NoTaxonException(final String msg, final RecordType recordType,
            final Integer lineNumber) {
        super(msg, AnnotationCode.BadField, recordType, lineNumber.toString());
    }

    @Override
    public final AnnotationType getType() {
        return AnnotationType.Error;
    }

}
