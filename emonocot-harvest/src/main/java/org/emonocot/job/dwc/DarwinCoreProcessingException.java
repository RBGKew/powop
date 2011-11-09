package org.emonocot.job.dwc;

import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;

/**
 *
 * @author ben
 *
 */
public abstract class DarwinCoreProcessingException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 5761352758463935293L;

    /**
     *
     */
    private AnnotationCode code;

    /**
     *
     */
    private String value;

    /**
     *
     */
    private RecordType recordType;

    /**
     *
     */
    public DarwinCoreProcessingException() {
        super();
    }

    /**
     *
     * @param message
     *            Set the message
     * @param code
     *            Set the code
     */
    public DarwinCoreProcessingException(final String message,
            AnnotationCode code, RecordType recordType, String value) {
        super(message);
        this.code = code;
        this.recordType = recordType;
        this.value = value;
    }

    /**
     *
     * @param cause
     *            Set the cause
     */
    public DarwinCoreProcessingException(final Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            Set the message
     * @param cause
     *            Set the cause
     */
    public DarwinCoreProcessingException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @return a short code representing this class of error
     */
    public final AnnotationCode getCode() {
        return code;
    }

    /**
     *
     * @return the type of annotation to create
     */
    public abstract AnnotationType getType();

    /**
     *
     * @return the record type
     */
    public final RecordType getRecordType() {
        return recordType;
    }

    /**
     *
     * @return the value
     */
    public final String getValue() {
        return value;
    }

}
