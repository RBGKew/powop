package org.emonocot.model.common;

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
     * If we created a new record.
     */
    Create,
    /**
     * If we updated an existing record.
     */
    Update,
    /**
     * If we deleted an existing record.
     */
    Delete,
    /**
     * If an expected record was present.
     */
    Present,
    /**
     * If an expected record was not present.
     */
    Absent,
    /**
     * If an unexpected record was present (but ignored).
     */
    Unexpected
}
