package org.emonocot.job.taxonmatch;

/**
 *
 * @author ben
 *
 */
public class TaxonDTO {

    /**
     *
     */
    private String identifier;

    /**
     *
     */
    private String name;

    /**
     * @return the identifier
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * @param newIdentifier the identifier to set
     */
    public final void setIdentifier(final String newIdentifier) {
        this.identifier = newIdentifier;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param newName the name to set
     */
    public final void setName(final String newName) {
        this.name = newName;
    }
}
