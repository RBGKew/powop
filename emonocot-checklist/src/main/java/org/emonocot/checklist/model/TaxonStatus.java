package org.emonocot.checklist.model;

/**
 *
 * @author ben
 *
 */
public enum TaxonStatus {
    /**
     * The taxon is accepted.
     */
    A('A', "Accepted"),
    /**
     * The taxon is illegitimate.
     */
    C('C', "Illegitimate"),
    /**
     * The taxon should be deleted (housekeeping).
     */
    D('D', "Delete"),
    /**
     * The record is incomplete (housekeeping).
     */
    I('I', "Incomplete"),
    /**
     * The name is missapplied.
     */
    M('M', "Misapplied"),
    /**
     * The name is an orthographic variant of the accepted name.
     */
    O('O', "Orthographic"),
    /**
     * The name is a synonym.
     */
    S('S', "Synonym"),
    /**
     * The name is unplaced.
     */
    U('U', "Unplaced"),
    /**
     * The name is invalid.
     */
    V('V', "Invalid");

    /**
     *
     */
    private char id;

    /**
     *
     */
    private String name;

    /**
     *
     * @param newId the identifier of the term
     * @param newName the name of the term
     */
    private TaxonStatus(final char newId, final String newName) {
        this.id = newId;
        this.name = newName;
    }

}
