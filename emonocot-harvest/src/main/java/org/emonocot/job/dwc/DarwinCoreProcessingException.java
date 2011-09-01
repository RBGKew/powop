package org.emonocot.job.dwc;

import org.emonocot.model.common.AnnotationType;

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
    public DarwinCoreProcessingException() {
        super();
    }

    /**
     *
     * @param message Set the message
     */
    public DarwinCoreProcessingException(final String message) {
        super(message);
    }

    /**
     *
     * @param cause Set the cause
     */
    public DarwinCoreProcessingException(final Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message Set the message
     * @param cause Set the cause
     */
    public DarwinCoreProcessingException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
    *
    * @return a short code representing this class of error
    */
   public final String getCode() {
       return this.getClass().getSimpleName().toUpperCase();
   }

   /**
    *
    * @return the type of annotation to create
    */
   public abstract AnnotationType getType();

}
