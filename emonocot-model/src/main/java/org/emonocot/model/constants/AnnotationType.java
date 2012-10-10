package org.emonocot.model.constants;

/**
 *
 * @author ben
 *
 */
public enum AnnotationType {
    /**
     * If we were unable to process the record.
     */
    Error,
    /**
     * If we were able to process the record in part.
     */
    Warn,
    /**
     * Information, but no actual problem.
     */
    Info;
}
