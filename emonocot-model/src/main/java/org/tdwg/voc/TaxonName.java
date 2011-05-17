package org.tdwg.voc;

import org.tdwg.Name;

/**
 *
 * @author ben
 *
 */
public class TaxonName extends Name {

    /**
     *
     */
    private String tnAuthorship;

    /**
     *
     */
    private String tnNameComplete;

    /**
     *
     * @return the authorship of this name
     */
    public final String getAuthorship() {
        return tnAuthorship;
    }

    /**
     *
     * @param authorship set the authorship of this name
     */
    public final void setAuthorship(final String authorship) {
        this.tnAuthorship = authorship;
    }

    /**
     *
     * @return the complete taxonomic name, including authorship
     */
    public final String getNameComplete() {
        return tnNameComplete;
    }

    /**
     *
     * @param nameComplete Set the complete taxonomic name
     */
    public final void setNameComplete(final String nameComplete) {
        this.tnNameComplete = nameComplete;
    }

}
