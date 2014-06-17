package org.emonocot.model.constants;

/**
 *
 * @author ben
 *
 */
public enum AnnotationCode {
    /**
     * Indicates that a record was updated.
     */
    Update,
    /**
     * Indicates that a record was created.
     */
    Create,
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
    Unexpected,
    /**
     * If an identifier property does not resolve to an object.
     */
    BadIdentifier,
    /**
     * If a record was already processed.
     */
    AlreadyProcessed,
    /**
     * If there was a problem parsing a whole record.
     */
    BadRecord,
    /**
     * If there was a problem converting a (non-identifier) value of a field.
     */
    BadField,
    /**
     * If the record has not changed, we skip it
     */
    Skipped,
    /**
     * If we find that the record belongs to another organisation
     */
    WrongAuthority,
    /**
     * There was a problem with the data held for a record 
     */
    BadData
}
